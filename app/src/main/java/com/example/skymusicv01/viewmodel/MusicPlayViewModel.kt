package com.example.skymusicv01.viewmodel

import android.content.Intent
import android.graphics.Point
import android.graphics.PointF
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.skymusicv01.FxManager
import com.example.skymusicv01.model.Song
import kotlinx.coroutines.*

class MusicPlayViewModel : ViewModel() {

    // 当前音乐
    private val _currentSong = mutableStateOf<Song?>(null)
    val currentSong: State<Song?> get() = _currentSong

    // 播放状态：是否暂停
    private val _isPaused = mutableStateOf(true)
    val isPaused: State<Boolean> get() = _isPaused

    // 当前播放时间（秒）
    private val _currentTime = mutableStateOf(0L)
    val currentTime: State<Long> get() = _currentTime

    // 总时长
    private val _totalDuration = mutableStateOf(0L)
    val totalDuration: State<Long> get() = _totalDuration

    // 播放速度
    private val _speed = mutableStateOf(1f)
    val speed:State<Float> get() = _speed

    // 记录每个时间点的音符
    private val timeToKeysMap  = mutableMapOf<Long, MutableList<String>>()

    // 音符时间差列表
    private val songNotesDiff = mutableListOf<Long>()

    // 协程作用域
    private var job: Job? = null
    private var currentIndex = 0  // 当前音符的索引

    // 坐标映射
    private val numberToPointMap = mutableMapOf<Int, PointF>()

    // 是否设置键位
    private val _isSetKeyBoard = mutableStateOf(false)
    val isSetKeyBoard:State<Boolean> get()=_isSetKeyBoard

    //设置键位时的长宽
    private val _width = mutableStateOf(250.dp)
    val width: State<Dp> get()= _width

    private val _height = mutableStateOf(250.dp)
    val height: State<Dp> get()= _height



    //加载歌曲
    fun loadSong(song: Song) {
        currentIndex=0
        _currentSong.value = song
        _totalDuration.value = song.songNotes.lastOrNull()?.time ?: 0L
        timeToKeysMap.clear()
        songNotesDiff.clear()  // 清空时间差列表

        // 遍历 song.songNotes，将相同时间的 key 合并到一个 List 中
        song.songNotes.forEach { note ->
            // 如果 Map 中已经包含当前时间，则将该 key 加入对应的 List
            if (timeToKeysMap.contains(note.time)) {
                timeToKeysMap[note.time]?.add(note.key)
            } else {
                // 如果 Map 中没有当前时间，则创建新的 List 并添加 key
                timeToKeysMap[note.time] = mutableListOf(note.key)
            }
        }
        // map是有序的，直接计算时间差即可
        var lastTime = 0L
        for (key in timeToKeysMap.keys) {
            songNotesDiff.add(key-lastTime)
            lastTime = key
        }

    }

    // 播放/暂停
    fun togglePause() {
        if(_isSetKeyBoard.value){
            _isPaused.value = !_isPaused.value
            if (_isPaused.value) {
                pause()
            } else {
                if(_currentTime.value>=_totalDuration.value){
                    restartSong()
                }else{
                    start()
                }
            }
        }
    }

    //开始播放
    private fun start() {
        startCheckInterval()
    }

    // 重新播放
    private fun restartSong() {
        _currentTime.value = 0L
        currentIndex = 0
        startCheckInterval()
    }

    private fun pause() {
        job?.cancel() // 取消协程
    }

    // 启动检查任务
    private fun startCheckInterval() {
        job = CoroutineScope(Dispatchers.Main).launch {
            while (_currentTime.value<_totalDuration.value) {
                val delayTime = songNotesDiff.getOrNull(currentIndex)?:1
                delay((delayTime/_speed.value).toLong())
                _currentTime.value+=delayTime
                checkAndOutputKey(_currentTime.value)
                currentIndex++
            }
            if(_currentTime.value>=_totalDuration.value){
                _isPaused.value = true
            }
        }
    }

    // 将当前时间对应的songNotes转为坐标位置发送给无障碍
    private fun checkAndOutputKey(currentTime: Long) {
        if(timeToKeysMap.contains(currentTime)){
            val points = mutableListOf<PointF>()
            timeToKeysMap[currentTime]?.forEach{ songNote->
                val number = Regex("Key(\\d+)").find(songNote)?.groupValues?.get(1)?.toIntOrNull()
                number?.let {numberToPointMap[number]?.let { points.add(it) }}
            }
            //Log.d("TestFloating","发出,time:$currentTime,notes:${points}")
            sendPointsToAccessibilityService(points)
        }
    }


    //发送坐标给无障碍
    private fun sendPointsToAccessibilityService(points: List<PointF>) {
        val intent = Intent("com.example.skymusicv01.ACTION_SEND_POINTS").apply {
            putParcelableArrayListExtra("points", ArrayList(points))
        }

        FxManager.context.sendBroadcast(intent)
    }

    // 加速
    fun increaseSpeed(){
        _speed.value = (_speed.value + 0.1f).let { "%.1f".format(it).toFloat() }
    }

    // 减速
    fun decreaseSpeed(){
        if (_speed.value - 0.1f > 0.1f) {
            _speed.value = (_speed.value - 0.1f).let { "%.1f".format(it).toFloat() } // 保留1位小数
        }
    }

    // 更新键位大小
    fun updateKeyBoardSize(newWidth:Dp,newHeight:Dp){
        _width.value = newWidth.coerceIn(180.dp,800.dp)
        _height.value = newHeight.coerceIn(180.dp,800.dp)
    }

    // 更新坐标映射
    fun updateNumberToPointMap(position:PointF,size: Point){
        val blockWidth = size.x / 5.0f
        val blockHeight = size.y / 3.0f

        // 计算 3 行 5 列的每个方块中心位置
        for (row in 0 until 3) {
            for (col in 0 until 5) {
                val centerX = position.x + (col * blockWidth) + blockWidth / 2.0f
                val centerY = position.y + (row * blockHeight) + blockHeight / 2.0f
                numberToPointMap[row*5+col] = PointF(centerX,centerY)
            }
        }
        _isSetKeyBoard.value=true
    }
}




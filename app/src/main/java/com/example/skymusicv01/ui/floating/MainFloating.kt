package com.example.skymusicv01.ui.floating

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.skymusicv01.FxManager
import com.example.skymusicv01.service.AutoAccessibilityService
import com.example.skymusicv01.viewmodel.MusicPlayViewModel

@Composable
fun MainFloating(musicViewModel: MusicPlayViewModel = viewModel()){

    var currentIndex by remember { mutableStateOf(0) }

    val currentSong by musicViewModel.currentSong
    val isPaused by musicViewModel.isPaused
    val currentTime by musicViewModel.currentTime
    val totalDuration by musicViewModel.totalDuration
    val speed by musicViewModel.speed
    val isSetKeyBoard by musicViewModel.isSetKeyBoard

    when(currentIndex){
        0-> CircleFloating(onClick = {currentIndex=1})
        1-> MusicPlayFloating(
            name = currentSong?.name?:"Unknown Song",
            author = currentSong?.author?:"Unknown Author",
            transcribedBy = currentSong?.transcribedBy?:"Unknown TranscribedBy",
            currentTime = currentTime,
            totalDuration = totalDuration,
            isPaused=isPaused,
            speed = speed,
            backClick = {
                currentIndex = 0
            },
            closeClick = {
                currentIndex = 0
                FxManager.cancelAll()
            },
            searchMusicClick = {
                currentIndex = 2
            },
            setKeyBoard = {
                FxManager.showSetKeyBoard(musicViewModel)
            },
            startMusicClick = {
                musicViewModel.togglePause()
                if(!isSetKeyBoard){
                    FxManager.showMessage("暂未设置键位，请先正确设置键位")
                }
            },
            decreasePlaySpeed = {
                musicViewModel.decreaseSpeed()
            },
            increasePlaySpeed = {
                musicViewModel.increaseSpeed()
            }
        )
        2-> {
            FxManager.setDisplayOnly()
            SearchMusicFloating(
                backClick = {
                    FxManager.setNormal()
                    currentIndex = 1
                },
                itemClick = {song->
                    musicViewModel.loadSong(song)
                    FxManager.setNormal()
                    currentIndex = 1
                }
            )
        }
    }
}
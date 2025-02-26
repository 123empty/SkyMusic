package com.example.skymusicv01.ui.floating

import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.LastPage
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.CloseFullscreen
import androidx.compose.material.icons.rounded.FastForward
import androidx.compose.material.icons.rounded.FastRewind
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LastPage
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.skymusicv01.R
import com.example.skymusicv01.core.LongToTimeString

@Composable
fun MusicPlayFloating(
    name:String,
    author:String,
    transcribedBy:String,
    currentTime: Long =0L,
    totalDuration: Long = 0L,
    isPaused:Boolean = true,
    speed:Float = 1.0f,
    backClick:()->Unit={},
    closeClick:()->Unit={},
    searchMusicClick:()->Unit={},
    setKeyBoard:()->Unit={},
    lastMusicClick:()->Unit={},
    nextMusicClick:()->Unit={},
    startMusicClick:()->Unit={},
    decreasePlaySpeed:()->Unit = {},
    increasePlaySpeed:()->Unit={}
){
    Column(
        modifier = Modifier
            .size(250.dp, 300.dp)
            .background(Color.White.copy(alpha = 0.6f))
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            MusicInfo(name, author, transcribedBy)

            Row(
                modifier = Modifier.align(Alignment.TopEnd)
            ){
                IconButton(
                    onClick = backClick,
                    modifier = Modifier
                        .size(30.dp)
                ) {
                    Icon(imageVector = Icons.Rounded.CloseFullscreen, contentDescription = null)
                }
                IconButton(
                    onClick = closeClick,
                    modifier = Modifier
                        .size(30.dp)
                ) {
                    Icon(imageVector = Icons.Rounded.Close, contentDescription = null)
                }
            }
        }
        MusicLinearProgress(
            currentTime = currentTime,
            totalDuration = totalDuration
        )
        MusicPlayButtons(
            lastMusicClick=lastMusicClick,
            nextMusicClick=nextMusicClick,
            startMusicClick=startMusicClick,
            isPaused=isPaused
        )
        CheckBoxGroups(
            searchMusicClick = searchMusicClick,
            setKeyBoard=setKeyBoard
        )
        MusicPlaySpeed(
            speed = speed,
            decreasePlaySpeed=decreasePlaySpeed,
            increasePlaySpeed=increasePlaySpeed
        )
    }
}

@Composable
fun MusicInfo(
    name:String,
    author:String,
    transcribedBy:String,
){
    Surface(
        modifier = Modifier
            .height(75.dp)
            .padding(2.dp),
        color = Color.Transparent
    ) {
        Row(){
            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription=null,
                modifier = Modifier.size(75.dp),
            )
            Spacer(Modifier.padding(horizontal = 5.dp))
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "歌手:$author",
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "改编者:$transcribedBy",
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

    }
}

@Composable
fun MusicLinearProgress(
    currentTime:Long,
    totalDuration:Long
){
    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Column {
            LinearProgressIndicator(
                progress = if (totalDuration > 0) currentTime.toFloat() / totalDuration.toFloat() else 0f,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .height(10.dp)
                    .clip(CircleShape)
            )
            Row{
                Text(
                    text = LongToTimeString(currentTime),
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.weight(1.0f))
                Text(
                    text = LongToTimeString(totalDuration),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun MusicPlayButtons(
    lastMusicClick:()->Unit={},
    nextMusicClick:()->Unit={},
    startMusicClick:()->Unit={},
    isPaused: Boolean
){
    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .fillMaxWidth()
    ){
        Row(horizontalArrangement = Arrangement.SpaceAround){
            IconButton(
                onClick = lastMusicClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.SkipPrevious,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }

            IconButton(
                onClick = startMusicClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = if(isPaused) Icons.Rounded.PlayArrow else Icons.Rounded.Pause,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }

            IconButton(
                onClick = nextMusicClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.SkipNext,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun CheckBoxGroups(
    searchMusicClick: () -> Unit ={},
    setKeyBoard:()->Unit={}
){
    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .fillMaxWidth()
    ){
        Row{
            Column(
                modifier = Modifier
                    .height(90.dp)
            ){

                Row(
                    modifier = Modifier
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("轨迹圆球", style = MaterialTheme.typography.titleMedium)
                    Checkbox(checked = false, onCheckedChange = {})
                }
                Row(
                    modifier = Modifier
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Text("轨迹圆球", style = MaterialTheme.typography.titleMedium)
                    Checkbox(checked = false, onCheckedChange = {})
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = searchMusicClick),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("搜索音乐", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(Icons.Rounded.MusicNote, contentDescription = null)
                }
            }


            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier.height(90.dp)
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("轨迹圆球", style = MaterialTheme.typography.titleMedium)
                    Checkbox(false, {})
                }
                Row(
                    modifier = Modifier
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("轨迹圆球", style = MaterialTheme.typography.titleMedium)
                    Checkbox(false, {})
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = setKeyBoard),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("设置键位", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(Icons.Rounded.Build, contentDescription = null)
                }
            }
        }

    }
}

@Composable
fun MusicPlaySpeed(
    speed:Float,
    decreasePlaySpeed:()->Unit = {},
    increasePlaySpeed:()->Unit={}
){
    Row(
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically

    ){
        Text("播放速度", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.weight(1.0f))
        IconButton(
            onClick = decreasePlaySpeed
        ) {
            Icon(imageVector = Icons.Rounded.FastRewind, contentDescription = null)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(speed.toString())
        Spacer(modifier = Modifier.width(10.dp))
        IconButton(
            onClick = increasePlaySpeed
        ) {
            Icon(imageVector = Icons.Rounded.FastForward, contentDescription = null)
        }
    }
}

@Preview
@Composable
fun tt(){
    CheckBoxGroups()
}

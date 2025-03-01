package com.example.skymusicv01.ui.floating

import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ZoomOutMap
import androidx.compose.material.icons.rounded.ZoomOutMap
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.skymusicv01.FxManager
import com.example.skymusicv01.viewmodel.MusicPlayViewModel

@Composable
fun KeyBoardFloating(
    musicPlayViewModel: MusicPlayViewModel
){

    val width by musicPlayViewModel.width
    val height by musicPlayViewModel.height

    val density = LocalDensity.current.density  // 获取屏幕密度因子
    Box(
        modifier = Modifier
            .size(width, height)
            .background(Color.Gray.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("按住右下角拉伸大小覆盖住键位")
            Row(
                modifier = Modifier
                    .padding(bottom = 20.dp)
            ) {
                Button(
                    onClick = {
                        val position = FxManager.getKeyBoardPosition()
                        val size = FxManager.getKeyBoardSize()
                        musicPlayViewModel.updateNumberToPointMap(position, size)
                        FxManager.cancelSetKeyBoard()
                    }
                ) {
                    Text("确定")
                }

                Button(
                    onClick = {
                        FxManager.cancelSetKeyBoard()
                    }
                ) {
                    Text("取消")
                }
            }
        }
        IconButton(
            onClick = {},
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.BottomEnd)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            // 转为dp
                            musicPlayViewModel.updateKeyBoardSize(
                                width + (dragAmount.x / density).dp,
                                height + (dragAmount.y / density).dp
                            )
                        }
                    )
                }
        ){
            Icon(
                imageVector = Icons.Rounded.ZoomOutMap,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
package com.example.skymusicv01.ui.floating

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.skymusicv01.R

@Composable
fun CircleFloating(onClick:()->Unit){
    Image(
        painter = painterResource(id = R.drawable.img2),
        contentDescription = null,
        modifier = Modifier
            .size(70.dp)
            .clickable(
                onClick = onClick,
                indication = null, // 去除点击效果
                interactionSource = remember { MutableInteractionSource() } // 去除按下时的反馈
            )
    )
}

@Preview
@Composable
fun CircleFloatingPreview(){
    CircleFloating(){
    }
}
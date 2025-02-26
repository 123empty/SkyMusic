package com.example.skymusicv01.ui.floating

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ShowMessage(message: String){
    Box(
        modifier = Modifier
            .background(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(6.dp)
    ) {
        // 显示消息文本
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 18.sp, // 设置字体大小
                color = Color.Black, // 设置字体颜色
            ),
            modifier = Modifier.padding(4.dp) // 添加内边距
        )
    }
}
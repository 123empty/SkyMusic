package com.example.skymusicv01.core

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun LongToTimeString(totalTime:Long): String{
    val seconds = totalTime/1000
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60

    return String.format("%02d:%02d",minutes, remainingSeconds)
}
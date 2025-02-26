package com.example.skymusicv01.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.accessibility.AccessibilityEvent
import android.util.Log
import android.graphics.Path
import android.graphics.PointF
import android.os.Build

class AutoAccessibilityService : AccessibilityService() {

    private val pointsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // 获取 points 数据并执行点击操作
            val points = intent.getParcelableArrayListExtra<PointF>("points")
            points?.let {
                performClick(it)
            }
        }
    }
    override fun onServiceConnected() {
        super.onServiceConnected()
        // 服务连接时的设置
        val filter = IntentFilter("com.example.skymusicv01.ACTION_SEND_POINTS")
        registerReceiver(pointsReceiver, filter)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // 监听无障碍事件
    }

    override fun onInterrupt() {
        // 当无障碍服务被中断时调用
        super.onDestroy()
        unregisterReceiver(pointsReceiver)
    }

    // 执行点击
    private fun performClick(points: List<PointF>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //Log.d("TestFloating", "收到，$points")
            val gesture = GestureDescription.Builder()

            // 遍历点击位置列表，为每个位置创建 StrokeDescription 并添加到 gesture
            points.forEach { point ->
                val path = Path().apply {
                    moveTo(point.x, point.y) // 为每个点击位置创建路径
                }

                // 每个点击位置的 StrokeDescription，设置相同的起始时间为0，持续时间为50ms
                val stroke = GestureDescription.StrokeDescription(path, 0, 50)
                gesture.addStroke(stroke) // 将 StrokeDescription 添加到手势中
            }

            // 构建 GestureDescription
            val gestureDescription = gesture.build()

            //Log.d("TestFloating", "开始发送")
            // 使用无障碍服务执行手势
//            dispatchGesture(gestureDescription, object : GestureResultCallback() {
//                override fun onCompleted(gestureDescription: GestureDescription?) {
//                    super.onCompleted(gestureDescription)
//                    Log.d("TestFloating", "点击完成")
//                }
//
//                override fun onCancelled(gestureDescription: GestureDescription?) {
//                    super.onCancelled(gestureDescription)
//                    Log.d("TestFloating", "点击取消")
//                }
//            }, null)
            dispatchGesture(gestureDescription,null,null)
        }
    }


}


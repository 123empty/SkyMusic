package com.example.skymusicv01

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.skymusicv01.navigation.NavGraph
import com.example.skymusicv01.navigation.Routes
import com.example.skymusicv01.ui.theme.SkyMusicV01Theme
import android.content.Intent
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import com.example.skymusicv01.service.AutoAccessibilityService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        // 检查无障碍服务是否启用，如果未启用，跳转到设置
//        if (!isAccessibilityServiceEnabled()) {
//            promptUserToEnableAccessibilityService()
//        }

        setContent {
            SkyMusicV01Theme {
                FxManager.context = applicationContext as Application
                val navController = rememberNavController()
                NavGraph(navController = navController, startDestination = Routes.Music.routes)
            }
        }
    }
    // 检查无障碍服务是否启用
    private fun isAccessibilityServiceEnabled(): Boolean {
        val am = getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
        return enabledServices.contains(AutoAccessibilityService::class.java.name)
    }

    // 提示用户启用无障碍服务
    private fun promptUserToEnableAccessibilityService() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }
}

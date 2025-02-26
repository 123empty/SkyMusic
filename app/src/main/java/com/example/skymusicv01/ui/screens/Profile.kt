package com.example.skymusicv01.ui.screens

import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import com.example.skymusicv01.FxManager

@Composable
fun Profile(){
    Button(
        onClick = {
            FxManager.install()
            FxManager.show()
        }
    ){

    }

}
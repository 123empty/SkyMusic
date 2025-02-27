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


    when(currentIndex){
        0-> CircleFloating(onClick = {currentIndex=1})
        1-> MusicPlayFloating(
            musicViewModel,
            backClick = {
                currentIndex = 0
            },
            closeClick = {
                currentIndex = 0
                FxManager.cancelAll()
            },
            searchMusicClick = {
                currentIndex = 2
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
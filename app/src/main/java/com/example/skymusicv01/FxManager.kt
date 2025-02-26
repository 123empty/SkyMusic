package com.example.skymusicv01

import android.app.Application
import android.graphics.Point
import android.graphics.PointF
import androidx.compose.ui.platform.ComposeView
import com.example.skymusicv01.ui.floating.KeyBoardFloating
import com.example.skymusicv01.ui.floating.MainFloating
import com.example.skymusicv01.ui.floating.ShowMessage
import com.example.skymusicv01.viewmodel.MusicPlayViewModel
import com.petterp.floatingx.FloatingX
import com.petterp.floatingx.assist.FxDisplayMode
import com.petterp.floatingx.assist.FxGravity
import com.petterp.floatingx.assist.FxScopeType
import com.petterp.floatingx.compose.enableComposeSupport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


object FxManager {

    private const val FX_MAIN_TAG="fx_main_tag"
    private const val FX_KEYBOARD_TAG = "fx_keyboard_tag"
    private const val FX_SHOW_MESSAGE_TAG="fx_show_message_tag"

    lateinit var context:Application


    fun install(){
        if(!FloatingX.isInstalled(FX_MAIN_TAG)){
            FloatingX.install {
                setContext(context)
                setScopeType(FxScopeType.SYSTEM)
                setEnableSafeArea(true)
                setTag(FX_MAIN_TAG)
                enableComposeSupport()
                setLayoutView(
                        ComposeView(context).apply {
                        setContent {
                            MainFloating()
                        }
                    }
                )
            }
        }
    }

    fun showSetKeyBoard(musicViewModel: MusicPlayViewModel){
        if(!FloatingX.isInstalled(FX_KEYBOARD_TAG)){
            FloatingX.install {
                setContext(context)
                setScopeType(FxScopeType.SYSTEM)
                setEnableSafeArea(true)
                setEnableEdgeAdsorption(false)
                setTag(FX_KEYBOARD_TAG)
                enableComposeSupport()
                setLayoutView(
                    ComposeView(context).apply {
                        setContent {
                            KeyBoardFloating(musicViewModel)
                        }
                    }
                )
            }.show()
        }
    }

    fun cancelSetKeyBoard(){
        FloatingX.controlOrNull(FX_KEYBOARD_TAG)?.cancel()
    }

    fun getKeyBoardPosition(): PointF {
        val x = FloatingX.controlOrNull(FX_KEYBOARD_TAG)?.getX()?:0f
        val y = FloatingX.controlOrNull(FX_KEYBOARD_TAG)?.getY()?:0f
        return PointF(x,y)
    }

    fun getKeyBoardSize(): Point {
        val w = FloatingX.controlOrNull(FX_KEYBOARD_TAG)?.getManagerView()?.width?:0
        val h = FloatingX.controlOrNull(FX_KEYBOARD_TAG)?.getManagerView()?.height?:0
        return Point(w,h)
    }

    fun show(){
        FloatingX.controlOrNull(FX_MAIN_TAG)?.show()
    }

    fun hide(){
        FloatingX.controlOrNull(FX_MAIN_TAG)?.hide()
    }

    fun cancel() {
        FloatingX.controlOrNull(FX_MAIN_TAG)?.cancel()
    }

    fun setDisplayOnly(){
        FloatingX.configControlOrNull(FX_MAIN_TAG)?.setDisplayMode(FxDisplayMode.DisplayOnly)
    }

    fun setNormal(){
        FloatingX.configControlOrNull(FX_MAIN_TAG)?.setDisplayMode(FxDisplayMode.Normal)
    }

    fun cancelAll(){
        FloatingX.uninstallAll()
    }

    fun showMessage(message:String,duration:Long = 3000L){
        if(!FloatingX.isInstalled(FX_SHOW_MESSAGE_TAG)){
            FloatingX.install {
                setContext(context)
                setScopeType(FxScopeType.SYSTEM)
                setEnableSafeArea(true)
                setEnableEdgeAdsorption(false)
                setDisplayMode(FxDisplayMode.DisplayOnly)
                setTag(FX_SHOW_MESSAGE_TAG)
                setGravity(FxGravity.CENTER)
                enableComposeSupport()
                setLayoutView(
                    ComposeView(context).apply {
                        setContent {
                            ShowMessage(message)
                        }
                    }
                )
            }.show()


            GlobalScope.launch(Dispatchers.Main) {
                delay(duration)
                FloatingX.controlOrNull(FX_SHOW_MESSAGE_TAG)?.cancel()
            }

        }
    }
}
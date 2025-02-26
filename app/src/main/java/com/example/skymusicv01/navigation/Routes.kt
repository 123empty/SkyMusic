package com.example.skymusicv01.navigation

sealed class Routes(val routes:String) {
    data object Music : Routes("music")
    data object Search : Routes("search")
    data object PlayLists:Routes("play_lists")
    data object Favorite:Routes("favorite")
    data object Profile:Routes("profile")
}
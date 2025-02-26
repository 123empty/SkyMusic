package com.example.skymusicv01.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.skymusicv01.model.BottomNavItem
import com.example.skymusicv01.ui.screens.Favorite
import com.example.skymusicv01.ui.screens.Music
import com.example.skymusicv01.ui.screens.PlayLists
import com.example.skymusicv01.ui.screens.Profile
import com.example.skymusicv01.ui.screens.Search

@Composable
fun NavGraph(navController: NavHostController,startDestination:String){

    Scaffold(
        bottomBar = { MyBottomBar(navController)}
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.Music.routes) {
                Music()
            }

            composable(Routes.Search.routes) {
                Search()
            }

            composable(Routes.Favorite.routes) {
                Favorite()
            }

            composable(Routes.PlayLists.routes) {
                PlayLists()
            }

            composable(Routes.Profile.routes) {
                Profile()
            }

        }
    }
}

@Composable
fun MyBottomBar(navController: NavHostController){

    val backStackEntry = navController.currentBackStackEntryAsState()

    val list = listOf(
        BottomNavItem(
            "音乐",
            Routes.Music.routes,
            Icons.Rounded.MusicNote
        ),
        BottomNavItem(
            "发现",
            Routes.Search.routes,
            Icons.Rounded.Search
        ),
        BottomNavItem(
            "喜欢",
            Routes.Favorite.routes,
            Icons.Rounded.Favorite
        ),
        BottomNavItem(
            "歌单",
            Routes.PlayLists.routes,
            Icons.Rounded.Menu
        ),
        BottomNavItem(
            "我的",
            Routes.Profile.routes,
            Icons.Rounded.Person
        )
    )

    BottomAppBar {
        list.forEach {

            val selected = it.route == backStackEntry.value?.destination?.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(it.route){
                        popUpTo(navController.graph.findStartDestination().id){
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(imageVector = it.icon , contentDescription = null)
                },
                label = { Text(it.title) }
            )
        }
    }
}
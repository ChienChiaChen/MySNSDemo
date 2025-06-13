package com.chiachen.mysnsdemo.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.chiachen.mysnsdemo.navigation.Screen
import com.chiachen.mysnsdemo.ui.me.MeScreen
import com.chiachen.mysnsdemo.ui.timeline.TimelineScreen


@Composable
fun MainScaffoldWithBottomBar(logout: () -> Unit) {
    val tabs = listOf(Screen.Timeline, Screen.Me)
    val innerNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEach { screen ->
                    val selected =
                        innerNavController.currentBackStackEntryAsState().value?.destination?.route == screen.route
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, null) }, // 替換 icon
                        label = { Text(screen.route) },
                        selected = selected,
                        onClick = {
                            innerNavController.navigate(screen.route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = innerNavController,
            startDestination = Screen.Timeline.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Timeline.route) { TimelineScreen() }
            composable(Screen.Me.route) { MeScreen(logout) }
        }
    }
}
package com.chiachen.mysnsdemo.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.chiachen.mysnsdemo.navigation.Screen
import com.chiachen.mysnsdemo.ui.MySnsAppState
import com.chiachen.mysnsdemo.ui.createpost.CreatePostScreen
import com.chiachen.mysnsdemo.ui.me.MeScreen
import com.chiachen.mysnsdemo.ui.timeline.TimelineScreen


@Composable
fun MainScaffoldWithBottomBar(appState: MySnsAppState) {
    val tabs = listOf(Screen.Timeline, Screen.Me)
    val innerNavController = rememberNavController()


    val currentRoute = innerNavController
        .currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        floatingActionButton = {
            if (currentRoute == Screen.Timeline.route) {
                FloatingActionButton(
                    onClick = {
                        innerNavController.navigate(Screen.CreatePost.route)
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Create Post")
                }
            }
        },

        bottomBar = {
            if (currentRoute != Screen.CreatePost.route) {
                NavigationBar {
                    tabs.forEach { screen ->
                        val selected =
                            innerNavController.currentBackStackEntryAsState().value?.destination?.route == screen.route
                        NavigationBarItem(
                            icon = {
                                if (screen.route == Screen.Timeline.route)
                                    Icon(Icons.Default.Home, null)
                                else
                                    Icon(Icons.Default.Person, null)
                            },
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
        }
    ) { innerPadding ->
        NavHost(
            navController = innerNavController,
            startDestination = Screen.Timeline.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Timeline.route) { TimelineScreen() }
            composable(Screen.Me.route) {
                val userInfo by appState.userInfoFlow.collectAsState()

                MeScreen(
                    email = userInfo?.email ?: "Unknown",
                    onLogout = { appState.logout() }
                )
            }

            composable(Screen.CreatePost.route) {
                CreatePostScreen(innerNavController)
            }
        }
    }
}
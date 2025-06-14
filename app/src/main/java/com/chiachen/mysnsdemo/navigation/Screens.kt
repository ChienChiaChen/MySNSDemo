package com.chiachen.mysnsdemo.navigation


sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")

    object Main : Screen("main")

    object Timeline : Screen("timeline")
    object CreatePost : Screen("create_post")

    object Me : Screen("me")
}
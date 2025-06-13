package com.chiachen.mysnsdemo.navigation


sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register") // ✅ 新增

    object Main : Screen("main")

    object Timeline : Screen("timeline")
    object Me : Screen("me")
}
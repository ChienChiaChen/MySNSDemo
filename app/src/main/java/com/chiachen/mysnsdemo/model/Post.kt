package com.chiachen.mysnsdemo.model

data class Post(
    val email: String = "",
    val content: String = "",
    val imageUrl: String = "",
    val timestamp: Long = 0L,
)
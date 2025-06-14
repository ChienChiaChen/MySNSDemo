package com.chiachen.mysnsdemo.model

data class Post(
    val displayName: String,
    val content: String,
    val imageUrl: String?,
    val timestamp: Long
)
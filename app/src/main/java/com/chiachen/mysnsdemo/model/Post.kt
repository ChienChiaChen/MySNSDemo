package com.chiachen.mysnsdemo.model

import com.chiachen.mysnsdemo.data.local.entity.PostEntity

data class Post(
    val id: String = "",
    val email: String = "",
    val content: String = "",
    val imageUrl: String = "",
    val timestamp: Long = 0L,
)

fun Post.toEntity(): PostEntity {
    return PostEntity(
        id = id,
        content = content,
        imageUrl = imageUrl.ifBlank { "" },
        timestamp = timestamp,
        email = email
    )
}
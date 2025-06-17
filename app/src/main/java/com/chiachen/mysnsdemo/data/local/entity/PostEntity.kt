package com.chiachen.mysnsdemo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chiachen.mysnsdemo.domain.model.Post

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: String,
    val content: String,
    val imageUrl: String?,
    val timestamp: Long,
    val email: String
)

fun PostEntity.toUiModel(): Post {
    return Post(
        email = this.email,
        content = this.content,
        imageUrl = this.imageUrl.orEmpty(),
        timestamp = this.timestamp
    )
}
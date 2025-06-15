package com.chiachen.mysnsdemo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_posts")
data class PendingPostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val content: String,
    val timestamp: Long,
    val email: String,
    val userId: String,
    val imageUri: String? = null
)
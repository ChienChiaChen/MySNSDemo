package com.chiachen.mysnsdemo.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chiachen.mysnsdemo.data.local.dao.PendingPostDao
import com.chiachen.mysnsdemo.data.local.dao.PostDao
import com.chiachen.mysnsdemo.data.local.entity.PendingPostEntity
import com.chiachen.mysnsdemo.data.local.entity.PostEntity

@Database(
    entities = [PostEntity::class, PendingPostEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun pendingPostDao(): PendingPostDao
}
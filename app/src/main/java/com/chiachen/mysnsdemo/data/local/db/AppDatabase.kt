package com.chiachen.mysnsdemo.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chiachen.mysnsdemo.data.local.dao.PostDao
import com.chiachen.mysnsdemo.data.local.entity.PostEntity

@Database(
    entities = [PostEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}
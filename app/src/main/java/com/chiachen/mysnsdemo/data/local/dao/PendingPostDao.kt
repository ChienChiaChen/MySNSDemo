package com.chiachen.mysnsdemo.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chiachen.mysnsdemo.data.local.entity.PendingPostEntity

@Dao
interface PendingPostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PendingPostEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<PendingPostEntity>)

    @Query("SELECT * FROM pending_posts ORDER BY timestamp ASC")
    suspend fun getAll(): List<PendingPostEntity>

    @Delete
    suspend fun delete(post: PendingPostEntity)

    @Query("DELETE FROM pending_posts WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM pending_posts")
    suspend fun deleteAll()
}

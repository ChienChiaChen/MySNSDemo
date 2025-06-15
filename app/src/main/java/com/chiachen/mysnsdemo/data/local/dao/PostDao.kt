package com.chiachen.mysnsdemo.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chiachen.mysnsdemo.data.local.entity.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    fun getPostsPagingSource(): PagingSource<Int, PostEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(posts: List<PostEntity>)

    @Query("DELETE FROM posts WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<String>)

    @Query("SELECT id FROM posts")
    suspend fun getAllIds(): List<String>

    @Query("DELETE FROM posts")
    suspend fun clearAll()
}

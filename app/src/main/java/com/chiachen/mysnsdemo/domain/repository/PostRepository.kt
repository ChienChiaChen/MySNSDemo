package com.chiachen.mysnsdemo.domain.repository

import android.net.Uri
import androidx.paging.PagingData
import com.chiachen.mysnsdemo.domain.model.Post
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPostPagingFlow(): Flow<PagingData<Post>>
    suspend fun clearAll()
    suspend fun createPost(
        content: String,
        imageUri: Uri?,
        currentUser: FirebaseUser
    ): Result<Unit>
}
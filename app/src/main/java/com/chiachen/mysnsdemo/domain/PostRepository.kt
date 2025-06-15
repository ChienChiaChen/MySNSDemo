package com.chiachen.mysnsdemo.domain

import androidx.paging.PagingData
import com.chiachen.mysnsdemo.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPostPagingFlow(): Flow<PagingData<Post>>
}
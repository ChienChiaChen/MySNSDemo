package com.chiachen.mysnsdemo.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.chiachen.mysnsdemo.data.local.dao.PostDao
import com.chiachen.mysnsdemo.data.local.entity.toUiModel
import com.chiachen.mysnsdemo.domain.PostRepository
import com.chiachen.mysnsdemo.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postDao: PostDao
) : PostRepository {

    override fun getPostPagingFlow(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { postDao.getPostsPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { it.toUiModel() }
        }
    }
}
package com.chiachen.mysnsdemo.ui.me

import androidx.lifecycle.ViewModel
import com.chiachen.mysnsdemo.data.local.dao.PendingPostDao
import com.chiachen.mysnsdemo.data.local.dao.PostDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MeViewModel @Inject constructor(
    private val postDao: PostDao,
    private val pendingPostDao: PendingPostDao,
) : ViewModel() {

    suspend fun logout() {
        postDao.deleteAll()
        pendingPostDao.deleteAll()
    }
}
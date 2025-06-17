package com.chiachen.mysnsdemo.ui.me

import androidx.lifecycle.ViewModel
import com.chiachen.mysnsdemo.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MeViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    suspend fun logout() {
        postRepository.clearAll()
    }
}
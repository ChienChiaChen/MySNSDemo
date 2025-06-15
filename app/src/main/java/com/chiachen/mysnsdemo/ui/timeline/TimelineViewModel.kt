package com.chiachen.mysnsdemo.ui.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.chiachen.mysnsdemo.domain.FirebaseRepository
import com.chiachen.mysnsdemo.domain.PostRepository
import com.chiachen.mysnsdemo.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TimelineViewModel @Inject constructor(
    private val repository: PostRepository,
    firebaseRepository: FirebaseRepository

) : ViewModel() {

    val postPagingFlow: Flow<PagingData<Post>> = repository
        .getPostPagingFlow()
        .cachedIn(viewModelScope)

    init {
        firebaseRepository.startObservingPosts()
    }
}

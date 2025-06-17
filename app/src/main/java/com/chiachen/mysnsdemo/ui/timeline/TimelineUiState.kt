package com.chiachen.mysnsdemo.ui.timeline

import com.chiachen.mysnsdemo.domain.model.Post

sealed interface TimelineUiState {
    object Loading : TimelineUiState
    data class Success(val posts: List<Post>) : TimelineUiState
    data class Error(val message: String) : TimelineUiState
}

package com.chiachen.mysnsdemo.ui.createpost

import android.net.Uri

data class CreatePostUiState(
    val postContent: String = "",
    val imageUri: Uri? = null,
    val isPostButtonEnable: Boolean = false,
    val status: CreatePostStatus = CreatePostStatus.IDLE,
    val errorMessage: String? = null
)

enum class CreatePostStatus {
    IDLE, LOADING, SUCCESS, ERROR
}

package com.chiachen.mysnsdemo.ui.createpost

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chiachen.mysnsdemo.domain.repository.PostRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val postRepository: PostRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreatePostUiState())
    val uiState: StateFlow<CreatePostUiState> = _uiState.asStateFlow()

    fun onPostContentChanged(content: String) {
        _uiState.update {
            it.copy(
                postContent = content,
                isPostButtonEnable = content.isNotBlank()
            )
        }
    }

    fun onImageSelected(uri: Uri?) {
        _uiState.update {
            it.copy(imageUri = uri)
        }
    }

    fun removeImage() {
        _uiState.update { it.copy(imageUri = null) }
    }

    fun resetUiState() {
        _uiState.value = CreatePostUiState()
    }

    fun createPost() {
        val user = auth.currentUser ?: return
        val content = _uiState.value.postContent
        val imageUri = _uiState.value.imageUri

        viewModelScope.launch {
            _uiState.update { it.copy(status = CreatePostStatus.LOADING) }

            val result = postRepository.createPost(content, imageUri, user)

            _uiState.update {
                if (result.isSuccess) {
                    it.copy(status = CreatePostStatus.SUCCESS)
                } else {
                    it.copy(
                        status = CreatePostStatus.ERROR,
                        errorMessage = result.exceptionOrNull()?.message ?: "Post will retry later."
                    )
                }
            }
        }
    }
}
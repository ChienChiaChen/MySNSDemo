package com.chiachen.mysnsdemo.ui.createpost

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth
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
        val currentUser = auth.currentUser ?: return
        val content = _uiState.value.postContent
        val imageUri = _uiState.value.imageUri

        viewModelScope.launch {
            _uiState.update { it.copy(status = CreatePostStatus.LOADING) }

            try {
                val imageUrl = imageUri?.let { uploadImage(it, currentUser.uid) } ?: ""

                val post = hashMapOf(
                    "content" to content,
                    "timestamp" to System.currentTimeMillis(),
                    "email" to (currentUser.email ?: ""),
                    "userId" to currentUser.uid,
                    "imageUrl" to imageUrl
                )

                firestore.collection("posts")
                    .add(post)
                    .await()

                _uiState.update { it.copy(status = CreatePostStatus.SUCCESS) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        status = CreatePostStatus.ERROR,
                        errorMessage = e.message
                    )
                }
            }
        }
    }

    private suspend fun uploadImage(uri: Uri, userId: String): String {
        val fileName = "post_images/${userId}_${System.currentTimeMillis()}.jpg"
        val ref = storage.reference.child(fileName)
        ref.putFile(uri).await()
        return ref.downloadUrl.await().toString()
    }
}
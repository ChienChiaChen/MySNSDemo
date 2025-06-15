package com.chiachen.mysnsdemo.ui.createpost

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chiachen.mysnsdemo.data.local.dao.PendingPostDao
import com.chiachen.mysnsdemo.data.local.entity.PendingPostEntity
import com.chiachen.mysnsdemo.util.NetworkMonitor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val auth: FirebaseAuth,
    private val networkMonitor: NetworkMonitor,
    private val pendingPostDao: PendingPostDao,
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

            val pending = PendingPostEntity(
                content = content,
                timestamp = System.currentTimeMillis(),
                email = currentUser.email ?: "",
                userId = currentUser.uid,
                imageUri = imageUri?.toString() ?: ""
            )
            val rowId = pendingPostDao.insert(pending)


            if (networkMonitor.networkIsAvailable()) {
                try {
                    val imageUrl = imageUri?.let { uploadImage(it, currentUser.uid) } ?: ""

                    val post = hashMapOf(
                        "content" to content,
                        "timestamp" to pending.timestamp,
                        "email" to pending.email,
                        "userId" to pending.userId,
                        "imageUrl" to imageUrl
                    )

                    firestore.collection("posts").add(post).await()
                    pendingPostDao.deleteById(rowId) // 上傳成功後移除暫存

                    _uiState.update { it.copy(status = CreatePostStatus.SUCCESS) }
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(status = CreatePostStatus.ERROR, errorMessage = "Post will retry later.")
                    }
                }
            } else {
                _uiState.update { it.copy(status = CreatePostStatus.SUCCESS) }
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
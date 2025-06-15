package com.chiachen.mysnsdemo.ui.timeline

import androidx.lifecycle.ViewModel
import com.chiachen.mysnsdemo.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TimelineViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _uiState = MutableStateFlow<TimelineUiState>(TimelineUiState.Loading)
    val uiState: StateFlow<TimelineUiState> = _uiState.asStateFlow()

    init {
        observePosts()
    }

    private fun observePosts() {
        firestore.collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _uiState.value = TimelineUiState.Error(error.message ?: "Unknown error")
                    return@addSnapshotListener
                }

                val posts = snapshot?.toObjects(Post::class.java) ?: emptyList()
                _uiState.value = TimelineUiState.Success(posts)
            }
    }

    fun reloadPosts(onDone: () -> Unit) {
        firestore.collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                val posts = snapshot.toObjects(Post::class.java)
                _uiState.value = TimelineUiState.Success(posts)
                onDone()
            }
            .addOnFailureListener { error ->
                _uiState.value = TimelineUiState.Error(error.message ?: "Unknown error")
                onDone()
            }
    }
}

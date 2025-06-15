package com.chiachen.mysnsdemo.data.repository

import com.chiachen.mysnsdemo.data.local.dao.PostDao
import com.chiachen.mysnsdemo.domain.FirebaseRepository
import com.chiachen.mysnsdemo.model.Post
import com.chiachen.mysnsdemo.model.toEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val postDao: PostDao
) : FirebaseRepository {
    override fun startObservingPosts() {
        firestore.collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error == null && snapshot != null) {
                    val posts = snapshot.toObjects(Post::class.java)
                    CoroutineScope(Dispatchers.IO).launch {
                        postDao.insertAll(posts.map { it.toEntity() })
                    }
                }
            }
    }
}
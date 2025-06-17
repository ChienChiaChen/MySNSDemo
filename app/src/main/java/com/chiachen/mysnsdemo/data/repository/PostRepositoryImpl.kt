package com.chiachen.mysnsdemo.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.chiachen.mysnsdemo.data.local.dao.PendingPostDao
import com.chiachen.mysnsdemo.data.local.dao.PostDao
import com.chiachen.mysnsdemo.data.local.entity.PendingPostEntity
import com.chiachen.mysnsdemo.data.local.entity.toUiModel
import com.chiachen.mysnsdemo.domain.repository.PostRepository
import com.chiachen.mysnsdemo.domain.model.Post
import com.chiachen.mysnsdemo.util.NetworkMonitor
import com.chiachen.mysnsdemo.util.copyUriToFile
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val networkMonitor: NetworkMonitor,
    private val postDao: PostDao,
    private val pendingPostDao: PendingPostDao
) : PostRepository {

    override fun getPostPagingFlow(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { postDao.getPostsPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { it.toUiModel() }
        }
    }
    override suspend fun clearAll() {
        postDao.deleteAll()
        pendingPostDao.deleteAll()
    }

    override suspend fun createPost(
        content: String,
        imageUri: Uri?,
        currentUser: FirebaseUser
    ): Result<Unit> {
        val imageFile = imageUri?.let { copyUriToFile(context, it) }
        val imageUriStr = imageFile?.toUri()?.toString() ?: ""

        val pending = PendingPostEntity(
            content = content,
            timestamp = System.currentTimeMillis(),
            email = currentUser.email ?: "",
            userId = currentUser.uid,
            imageUri = imageUriStr
        )
        val rowId = pendingPostDao.insert(pending)

        if (!networkMonitor.networkIsAvailable()) {
            return Result.success(Unit)
        }

        return try {
            val imageUrl = imageFile?.let { uploadImage(it.toUri(), currentUser.uid) } ?: ""

            val post = hashMapOf(
                "content" to content,
                "timestamp" to pending.timestamp,
                "email" to pending.email,
                "userId" to pending.userId,
                "imageUrl" to imageUrl
            )

            firestore.collection("posts").add(post).await()
            pendingPostDao.deleteById(rowId)
            imageFile?.takeIf { it.exists() }?.delete()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun uploadImage(uri: Uri, userId: String): String {
        val fileName = "post_images/${userId}_${System.currentTimeMillis()}.jpg"
        val ref = storage.reference.child(fileName)
        ref.putFile(uri).await()
        return ref.downloadUrl.await().toString()
    }

}
package com.chiachen.mysnsdemo.data.worker

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.chiachen.mysnsdemo.data.local.dao.PendingPostDao
import com.chiachen.mysnsdemo.di.Dispatcher
import com.chiachen.mysnsdemo.di.MySnsDispatchers
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URI


@HiltWorker
class PostUploadWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val pendingPostDao: PendingPostDao,
    @Dispatcher(MySnsDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        try {
            val pendingPosts = pendingPostDao.getAll()
            for (post in pendingPosts) {
                val imageUrl = post.imageUri?.takeIf { it.isNotBlank() }?.let { path ->
                    val file = File(URI(path))
                    if (!file.exists()) return@let ""

                    val ref = storage.reference.child("post_images/${post.userId}_${System.currentTimeMillis()}.jpg")
                    ref.putFile(file.toUri()).await()
                    val downloadUrl = ref.downloadUrl.await().toString()
                    file.delete()
                    downloadUrl
                } ?: ""

                val postMap = mapOf(
                    "content" to post.content,
                    "timestamp" to post.timestamp,
                    "email" to post.email,
                    "userId" to post.userId,
                    "imageUrl" to imageUrl
                )

                firestore.collection("posts").add(postMap).await()
                pendingPostDao.delete(post)
            }
            Result.success()
        } catch (e: Exception) {
            Log.e("PostUploadWorker", "Upload failed: ${e.message}")
            Result.retry()
        }
    }
}

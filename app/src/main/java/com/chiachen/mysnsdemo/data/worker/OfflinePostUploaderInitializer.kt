package com.chiachen.mysnsdemo.data.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.chiachen.mysnsdemo.di.Dispatcher
import com.chiachen.mysnsdemo.di.MySnsDispatchers
import com.chiachen.mysnsdemo.util.NetworkMonitor
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class OfflinePostUploaderInitializer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val networkMonitor: NetworkMonitor,
    @Dispatcher(MySnsDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) {
    fun observeAndUploadWhenNetworkRestored() {
        CoroutineScope(ioDispatcher).launch {
            networkMonitor.isOnline.collectLatest { isConnected ->
                if (isConnected) {
                    scheduleUploadWorker()
                }
            }
        }
    }

    private fun scheduleUploadWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val uploadRequest = OneTimeWorkRequestBuilder<PostUploadWorker>()
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            "upload_offline_posts_once",
            ExistingWorkPolicy.REPLACE,
            uploadRequest
        )
    }
}
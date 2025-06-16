package com.chiachen.mysnsdemo

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.chiachen.mysnsdemo.data.worker.OfflinePostUploaderInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MySnsApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var uploaderInitializer: OfflinePostUploaderInitializer

    override fun onCreate() {
        super.onCreate()
        uploaderInitializer.observeAndUploadWhenNetworkRestored()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
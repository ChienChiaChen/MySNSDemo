package com.chiachen.mysnsdemo.di

import com.chiachen.mysnsdemo.util.ConnectivityManagerNetworkMonitor
import com.chiachen.mysnsdemo.util.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor,
    ): NetworkMonitor
}

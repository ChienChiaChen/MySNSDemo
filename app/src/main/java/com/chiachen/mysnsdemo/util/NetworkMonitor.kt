package com.chiachen.mysnsdemo.util

import kotlinx.coroutines.flow.Flow

/**
 * Utility for reporting app connectivity status
 */
interface NetworkMonitor {
    val isOnline: Flow<Boolean>
    suspend fun networkIsAvailable(): Boolean
}

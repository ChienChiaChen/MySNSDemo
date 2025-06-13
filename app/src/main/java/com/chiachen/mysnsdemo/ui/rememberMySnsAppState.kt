package com.chiachen.mysnsdemo.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.chiachen.mysnsdemo.util.NetworkMonitor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


@Composable
fun rememberMySnsAppState(
    networkMonitor: NetworkMonitor,
    auth: FirebaseAuth = Firebase.auth,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): MySnsAppState {
    return remember(
        coroutineScope,
        networkMonitor,
    ) {
        MySnsAppState(
            auth = auth,
            coroutineScope = coroutineScope,
            networkMonitor = networkMonitor,
        )
    }
}

@Stable
class MySnsAppState(
    private val auth: FirebaseAuth,
    coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
) {
    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    val authStateFlow: StateFlow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser)
        }
        auth.addAuthStateListener(listener)

        awaitClose {
            auth.removeAuthStateListener(listener)
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = auth.currentUser // 初始值：登入狀態
    )

    val isLoggedInFlow: StateFlow<Boolean> = authStateFlow
        .map { it != null }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = auth.currentUser != null
        )

    fun logout() {
        auth.signOut()
    }

}
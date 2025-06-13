package com.chiachen.mysnsdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.chiachen.mysnsdemo.ui.MySnsApp
import com.chiachen.mysnsdemo.ui.rememberMySnsAppState
import com.chiachen.mysnsdemo.ui.theme.MySNSDemoTheme
import com.chiachen.mysnsdemo.util.NetworkMonitor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val appState = rememberMySnsAppState(
                networkMonitor = networkMonitor,
            )

            MySNSDemoTheme {
                MySnsApp(appState)
            }
        }
    }
}
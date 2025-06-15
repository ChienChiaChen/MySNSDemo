package com.chiachen.mysnsdemo.ui.timeline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chiachen.mysnsdemo.ui.component.Post
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@Composable
fun TimelineScreen(viewModel: TimelineViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    val isRefreshing = remember { mutableStateOf(false) }

    fun refresh() {
        isRefreshing.value = true
        viewModel.reloadPosts {
            isRefreshing.value = false
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing.value),
        onRefresh = { refresh() }
    ) {
        when (uiState) {
            is TimelineUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is TimelineUiState.Success -> {
                val posts = (uiState as TimelineUiState.Success).posts
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(posts) { post ->
                        Post(post = post)
                    }
                }
            }

            is TimelineUiState.Error -> {
                val message = (uiState as TimelineUiState.Error).message
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Error: $message", color = Color.Red)
                }
            }
        }
    }
}
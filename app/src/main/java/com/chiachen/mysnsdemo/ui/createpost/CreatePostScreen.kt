package com.chiachen.mysnsdemo.ui.createpost

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chiachen.mysnsdemo.ui.component.SelectedImageWithRemoveButton

@Composable
fun CreatePostScreen(
    navController: NavController,
    viewModel: CreatePostViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onImageSelected(uri)
    }

    LaunchedEffect(uiState.status) {
        when (uiState.status) {
            CreatePostStatus.SUCCESS -> {
                navController.popBackStack()
            }

            CreatePostStatus.ERROR -> {
                uiState.errorMessage?.let {
                    snackbarHostState.showSnackbar(it)
                }
            }

            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    enabled =  uiState.status != CreatePostStatus.LOADING,
                    value = uiState.postContent,
                    onValueChange = viewModel::onPostContentChanged,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("What's on your mind?") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                uiState.imageUri?.let { uri ->
                    SelectedImageWithRemoveButton(uri, uiState.status != CreatePostStatus.LOADING) {
                        viewModel.removeImage()
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        enabled =  uiState.status != CreatePostStatus.LOADING,
                        onClick = { imagePickerLauncher.launch("image/*") }) {
                        Icon(Icons.Default.AddPhotoAlternate, contentDescription = "Pick image")
                    }

                    Button(
                        onClick = { viewModel.createPost() },
                        enabled = uiState.isPostButtonEnable && uiState.status != CreatePostStatus.LOADING
                    ) {
                        Text("Post")
                    }
                }
            }

            if (uiState.status == CreatePostStatus.LOADING) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

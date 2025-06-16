package com.chiachen.mysnsdemo

import android.net.Uri
import com.chiachen.mysnsdemo.domain.PostRepository
import com.chiachen.mysnsdemo.ui.createpost.CreatePostStatus
import com.chiachen.mysnsdemo.ui.createpost.CreatePostViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreatePostViewModelTest {

    private lateinit var postRepository: PostRepository
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: CreatePostViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val fakeUser = mockk<FirebaseUser>().apply {
        every { uid } returns "user123"
        every { email } returns "test@example.com"
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        postRepository = mockk()
        auth = mockk()
        every { auth.currentUser } returns fakeUser

        viewModel = CreatePostViewModel(auth, postRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onPostContentChanged updates state`() {
        viewModel.onPostContentChanged("Hello World")
        val state = viewModel.uiState.value
        assertEquals("Hello World", state.postContent)
        assertTrue(state.isPostButtonEnable)
    }

    @Test
    fun `onImageSelected sets imageUri`() {
        val fakeUri = mockk<Uri>()
        viewModel.onImageSelected(fakeUri)
        assertEquals(fakeUri, viewModel.uiState.value.imageUri)
    }

    @Test
    fun `removeImage clears imageUri`() {
        val fakeUri = mockk<Uri>()
        viewModel.onImageSelected(fakeUri)
        viewModel.removeImage()
        assertNull(viewModel.uiState.value.imageUri)
    }

    @Test
    fun `createPost success updates state to SUCCESS`() = runTest {
        val fakeUri = mockk<Uri>()
        coEvery { postRepository.createPost(any(), any(), any()) } returns Result.success(Unit)

        viewModel.onPostContentChanged("Test Content")
        viewModel.onImageSelected(fakeUri)
        viewModel.createPost()

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(CreatePostStatus.SUCCESS, state.status)
        assertNull(state.errorMessage)
    }

    @Test
    fun `createPost failure updates state to ERROR`() = runTest {
        coEvery { postRepository.createPost(any(), any(), any()) } returns Result.failure(Exception("Network failed"))

        viewModel.onPostContentChanged("Test Content")
        viewModel.createPost()

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(CreatePostStatus.ERROR, state.status)
        assertEquals("Network failed", state.errorMessage)
    }
}

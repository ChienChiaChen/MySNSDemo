package com.chiachen.mysnsdemo

import com.chiachen.mysnsdemo.domain.repository.AuthRepository
import com.chiachen.mysnsdemo.ui.register.RegisterViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
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
class RegisterViewModelTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var viewModel: RegisterViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        authRepository = mockk()
        viewModel = RegisterViewModel(authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial uiState is empty`() = runTest {
        val state = viewModel.uiState.value
        assertEquals("", state.email)
        assertEquals("", state.password)
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
    }

    @Test
    fun `email and password blank shows error`() = runTest {
        viewModel.register {}

        val state = viewModel.uiState.value
        assertEquals("Please fill in all fields", state.errorMessage)
    }

    @Test
    fun `password and confirm password mismatch shows error`() = runTest {
        viewModel.onEmailChanged("test@example.com")
        viewModel.onPasswordChanged("123456")
        viewModel.onConfirmPasswordChanged("abcdef")

        viewModel.register {}

        val state = viewModel.uiState.value
        assertEquals("Passwords do not match", state.errorMessage)
    }

    @Test
    fun `successful registration updates state`() = runTest {
        coEvery { authRepository.register(any(), any()) } returns Result.success(Unit)

        viewModel.onEmailChanged("test@example.com")
        viewModel.onPasswordChanged("123456")
        viewModel.onConfirmPasswordChanged("123456")

        var successCalled = false
        viewModel.register { successCalled = true }

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(successCalled)
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
    }

    @Test
    fun `failed registration shows error message`() = runTest {
        coEvery { authRepository.register(any(), any()) } returns Result.failure(Exception("Register failed"))

        viewModel.onEmailChanged("test@example.com")
        viewModel.onPasswordChanged("123456")
        viewModel.onConfirmPasswordChanged("123456")

        viewModel.register {}

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Register failed", state.errorMessage)
        assertFalse(state.isLoading)
    }
}

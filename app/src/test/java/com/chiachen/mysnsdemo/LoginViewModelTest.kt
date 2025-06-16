package com.chiachen.mysnsdemo

import com.chiachen.mysnsdemo.domain.AuthRepository
import com.chiachen.mysnsdemo.ui.login.LoginViewModel
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
class LoginViewModelTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var viewModel: LoginViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        authRepository = mockk()
        viewModel = LoginViewModel(authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is empty`() {
        val state = viewModel.uiState.value
        assertEquals("", state.email)
        assertEquals("", state.password)
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
    }

    @Test
    fun `onEmailChanged updates state`() {
        viewModel.onEmailChanged("user@example.com")
        assertEquals("user@example.com", viewModel.uiState.value.email)
    }

    @Test
    fun `onPasswordChanged updates state`() {
        viewModel.onPasswordChanged("123456")
        assertEquals("123456", viewModel.uiState.value.password)
    }

    @Test
    fun `login with blank fields sets error`() = runTest {
        viewModel.login {}
        assertEquals("Email and password must not be empty", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `login success calls onSuccess and sets loading false`() = runTest {
        coEvery { authRepository.login(any(), any()) } returns Result.success(Unit)

        viewModel.onEmailChanged("user@example.com")
        viewModel.onPasswordChanged("123456")

        var successCalled = false
        viewModel.login { successCalled = true }

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(successCalled)
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
    }

    @Test
    fun `login failure sets error message and disables loading`() = runTest {
        coEvery { authRepository.login(any(), any()) } returns Result.failure(Exception("Wrong password"))

        viewModel.onEmailChanged("user@example.com")
        viewModel.onPasswordChanged("wrongpass")

        viewModel.login {}

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Wrong password", state.errorMessage)
    }
}

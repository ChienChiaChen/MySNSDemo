package com.chiachen.mysnsdemo

import com.chiachen.mysnsdemo.domain.PostRepository
import com.chiachen.mysnsdemo.ui.me.MeViewModel
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MeViewModelTest {

    private lateinit var postRepository: PostRepository
    private lateinit var viewModel: MeViewModel

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        postRepository = mockk(relaxed = true)
        viewModel = MeViewModel(postRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `logout calls postRepository clearAll`() = runTest {
        viewModel.logout()

        coVerify(exactly = 1) {
            postRepository.clearAll()
        }
    }
}

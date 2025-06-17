package com.chiachen.mysnsdemo

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.chiachen.mysnsdemo.domain.repository.FirebaseRepository
import com.chiachen.mysnsdemo.domain.repository.PostRepository
import com.chiachen.mysnsdemo.domain.model.Post
import com.chiachen.mysnsdemo.ui.timeline.TimelineViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TimelineViewModelTest {

    private lateinit var postRepository: PostRepository
    private lateinit var firebaseRepository: FirebaseRepository
    private lateinit var viewModel: TimelineViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        postRepository = mockk()
        firebaseRepository = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init calls firebaseRepository startObservingPosts`() {
        // Arrange
        every { postRepository.getPostPagingFlow() } returns emptyFlow()

        // Act
        viewModel = TimelineViewModel(postRepository, firebaseRepository)

        // Assert
        verify(exactly = 1) {
            firebaseRepository.startObservingPosts()
        }
    }

    @Test
    fun `postPagingFlow emits expected items`() = runTest {
        // Arrange
        val fakePosts = listOf(
            Post(
                id = "1",
                content = "Hello1",
                email = "user1@example.com",
                timestamp = 1234567890L,
                imageUrl = ""
            ),
            Post(
                id = "2",
                content = "Hello2",
                email = "user2@example.com",
                timestamp = 1234567890L,
                imageUrl = ""
            ),
        )
        val pagingData = PagingData.from(fakePosts)
        val pagingFlow = flowOf(pagingData)
        every { postRepository.getPostPagingFlow() } returns pagingFlow

        viewModel = TimelineViewModel(postRepository, firebaseRepository)

        val differ = AsyncPagingDataDiffer(
            diffCallback = PostDiffCallback(),
            updateCallback = NoopListCallback(),
            mainDispatcher = StandardTestDispatcher(),
            workerDispatcher = StandardTestDispatcher()
        )

        differ.submitData(viewModel.postPagingFlow.first())

        advanceUntilIdle()

        assertEquals(fakePosts, differ.snapshot().items)
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem == newItem
}

class NoopListCallback : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

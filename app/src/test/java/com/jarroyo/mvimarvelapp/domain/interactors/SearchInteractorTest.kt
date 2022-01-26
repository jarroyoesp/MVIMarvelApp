package com.jarroyo.mvimarvelapp.domain.interactors

import android.accounts.NetworkErrorException
import com.jarroyo.mvimarvelapp.domain.repository.DataRepository
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class SearchInteractorTest {

    private lateinit var searchInteractor: SearchInteractor

    @MockK
    private lateinit var repository: DataRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        searchInteractor = SearchInteractor(
            repository
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `GIVEN remote error WHEN call invoke() THEN returns EitherLeft`() = runBlocking {
        // Given
        coEvery { repository.search(any()) } returns Result.failure(NetworkErrorException())

        // When
        val response = searchInteractor.invoke("name")

        // Then
        assert(response.isFailure)
    }

    @Test
    fun `GIVEN remote success WHEN call invoke() THEN returns EitherRight`() = runBlocking {
        // Given
        coEvery { repository.search(any()) } returns Result.success(emptyList())

        // When
        val response = searchInteractor.invoke("name")

        // Then
        assert(response.isSuccess)
    }
}

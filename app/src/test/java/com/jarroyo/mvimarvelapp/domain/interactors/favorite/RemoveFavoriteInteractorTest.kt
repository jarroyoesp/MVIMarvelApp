package com.jarroyo.mvimarvelapp.domain.interactors.favorite

import android.accounts.NetworkErrorException
import com.jarroyo.mvimarvelapp.domain.repository.DataRepository
import com.jarroyo.mvimarvelapp.mockCharacterUIModel
import com.jarroyo.mvimarvelapp.mockCharacterUIModelList
import com.jarroyo.mvimarvelapp.mockException
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class RemoveFavoriteInteractorTest {
    private lateinit var removeFavoriteInteractor: RemoveFavoriteInteractor

    @MockK
    private lateinit var repository: DataRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        removeFavoriteInteractor = RemoveFavoriteInteractor(
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
        coEvery { repository.removeFavorite(any()) } returns Result.failure(mockException)

        // When
        val response = removeFavoriteInteractor.invoke(mockCharacterUIModel)

        // Then
        assert(response.isFailure)
    }


    @Test
    fun `GIVEN remote success WHEN call invoke() THEN returns EitherRight`() = runBlocking {
        // Given
        coEvery { repository.removeFavorite(any()) } returns Result.success(true)

        // When
        val response = removeFavoriteInteractor.invoke(mockCharacterUIModel)

        // Then
        assert(response.isSuccess)
        assertEquals(response.getOrNull(), true)
    }
}


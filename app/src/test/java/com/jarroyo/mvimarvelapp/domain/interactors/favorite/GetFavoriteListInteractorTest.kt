package com.jarroyo.mvimarvelapp.domain.interactors.favorite

import com.jarroyo.mvimarvelapp.domain.repository.DataRepository
import com.jarroyo.mvimarvelapp.mockCharacterUIModelList
import com.jarroyo.mvimarvelapp.mockException
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetFavoriteListInteractorTest {
    private lateinit var getFavoriteListInteractor: GetFavoriteListInteractor

    @MockK
    private lateinit var repository: DataRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getFavoriteListInteractor = GetFavoriteListInteractor(
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
        coEvery { repository.getFavorite() } returns Result.failure(mockException)

        // When
        val response = getFavoriteListInteractor.invoke()

        // Then
        assert(response.isFailure)
    }

    @Test
    fun `GIVEN remote success WHEN call invoke() THEN returns EitherRight`() = runBlocking {
        // Given
        coEvery { repository.getFavorite() } returns Result.success(mockCharacterUIModelList)

        // When
        val response = getFavoriteListInteractor.invoke()

        // Then
        assert(response.isSuccess)
        assertEquals(response.getOrNull(), mockCharacterUIModelList)
    }
}

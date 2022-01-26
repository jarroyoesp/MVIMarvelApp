package com.jarroyo.mvimarvelapp.domain.interactors.favorite

import com.jarroyo.mvimarvelapp.domain.repository.DataRepository
import com.jarroyo.mvimarvelapp.mockCharacterUIModel
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

class SaveFavoriteInteractorTest {
    private lateinit var saveFavoriteInteractor: SaveFavoriteInteractor

    @MockK
    private lateinit var repository: DataRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        saveFavoriteInteractor = SaveFavoriteInteractor(
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
        coEvery { repository.saveFavorite(any()) } returns Result.failure(mockException)

        // When
        val response = saveFavoriteInteractor.invoke(mockCharacterUIModel)

        // Then
        assert(response.isFailure)
    }

    @Test
    fun `GIVEN remote success WHEN call invoke() THEN returns EitherRight`() = runBlocking {
        // Given
        coEvery { repository.saveFavorite(any()) } returns Result.success(true)

        // When
        val response = saveFavoriteInteractor.invoke(mockCharacterUIModel)

        // Then
        assert(response.isSuccess)
        assertEquals(response.getOrNull(), true)
    }
}

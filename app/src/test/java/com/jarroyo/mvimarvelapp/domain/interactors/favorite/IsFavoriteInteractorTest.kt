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

class IsFavoriteInteractorTest {
    private lateinit var isFavoriteInteractor: IsFavoriteInteractor

    @MockK
    private lateinit var repository: DataRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        isFavoriteInteractor = IsFavoriteInteractor(
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
        coEvery { repository.isFavorite(any()) } returns true

        // When
        val response = isFavoriteInteractor.invoke(mockCharacterUIModel)

        // Then
        assert(response)
    }


    @Test
    fun `GIVEN remote success WHEN call invoke() THEN returns EitherRight`() = runBlocking {
        // Given
        coEvery { repository.isFavorite(any()) } returns false

        // When
        val response = isFavoriteInteractor.invoke(mockCharacterUIModel)

        // Then
        assert(!response)
    }
}


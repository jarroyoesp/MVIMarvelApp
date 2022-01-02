package com.jarroyo.mvimarvelapp.domain.interactors

import android.accounts.NetworkErrorException
import com.jarroyo.mvimarvelapp.domain.repository.DataRepository
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetListInteractorTest {
    private lateinit var getListInteractor: GetListInteractor

    @MockK
    private lateinit var repository: DataRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getListInteractor = GetListInteractor(
            repository
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `GIVEN remote error WHEN call getHomeData THEN returns EitherLeft`() = runBlocking {
        // Given
        coEvery { repository.getList(any()) } returns Result.failure(NetworkErrorException())

        // When
        val response = getListInteractor.invoke(0)

        // Then
        assert(response.isFailure)
    }


    @Test
    fun `GIVEN remote success WHEN call getHomeData THEN returns EitherRight`() = runBlocking {
        // Given
        coEvery { repository.getList(any()) } returns Result.success(emptyList())

        // When
        val response = getListInteractor.invoke(0)

        // Then
        assert(response.isSuccess)
    }
}
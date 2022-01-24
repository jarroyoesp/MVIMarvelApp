package com.jarroyo.mvimarvelapp.data.remote

import org.junit.Assert.*

import com.jarroyo.mvimarvelapp.data.remote.model.APIListResponse
import com.jarroyo.mvimarvelapp.domain.model.toDomainModel
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response


class NetworkDataSourceImplTest {
    @MockK
    private lateinit var apiService: ApiService

    @MockK
    private lateinit var networkSystem: NetworkSystem

    private lateinit var networkDataSource: NetworkDataSourceImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        networkDataSource = NetworkDataSourceImpl(
            apiService,
            networkSystem
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `GIVEN remote error WHEN call getCharacterList THEN returns EitherLeft`() = runBlocking {
        // Given
        coEvery { apiService.getCharacterList(any())} returns MockAPiResponses.getErrorResponse()
        every { networkSystem.isNetworkAvailable() } returns true

        // When
        val response = networkDataSource.getCharacterList(0)

        // Then
        assert(response.isFailure)
    }

    @Test
    fun `GIVEN remote success WHEN call getCharacterList THEN returns EitherRight`() = runBlocking {
        // Given
        val apiResponse = APIListResponse()
        coEvery { apiService.getCharacterList(any())} returns Response.success(
            apiResponse
        )
        every { networkSystem.isNetworkAvailable() } returns true

        // When
        val response = networkDataSource.getCharacterList(0)

        // Then
        assert(response.isSuccess)
        assertEquals(response.getOrNull(), apiResponse.toDomainModel())
    }

    @Test
    fun `GIVEN No Internet WHEN call getHomeData THEN returns EitherLeft`() = runBlocking {
        // Given
        every { networkSystem.isNetworkAvailable() } returns false

        // When
        val response = networkDataSource.getCharacterList(0)

        // Then
        assert(response.isFailure)
    }

    @Test
    fun `GIVEN remote error WHEN call searchCharacterList THEN returns EitherLeft`() = runBlocking {
        // Given
        coEvery { apiService.searchCharacterList(any())} returns MockAPiResponses.getErrorResponse()
        every { networkSystem.isNetworkAvailable() } returns true

        // When
        val response = networkDataSource.searchCharacterList("name")

        // Then
        assert(response.isFailure)
    }

    @Test
    fun `GIVEN remote success WHEN call searchCharacterList THEN returns EitherRight`() = runBlocking {
        // Given
        val apiResponse = APIListResponse()
        coEvery { apiService.searchCharacterList(any())} returns Response.success(
            apiResponse
        )
        every { networkSystem.isNetworkAvailable() } returns true

        // When
        val response = networkDataSource.searchCharacterList("name")

        // Then
        assert(response.isSuccess)
        assertEquals(response.getOrNull(), apiResponse.toDomainModel())
    }

    @Test
    fun `GIVEN No Internet WHEN call searchCharacterList THEN returns EitherLeft`() = runBlocking {
        // Given
        every { networkSystem.isNetworkAvailable() } returns false

        // When
        val response = networkDataSource.searchCharacterList("name")

        // Then
        assert(response.isFailure)
    }
}

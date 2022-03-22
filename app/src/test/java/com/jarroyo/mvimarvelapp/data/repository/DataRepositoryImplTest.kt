package com.jarroyo.mvimarvelapp.data.repository

import android.accounts.NetworkErrorException
import com.jarroyo.mvimarvelapp.data.local.DiskDataSource
import com.jarroyo.mvimarvelapp.data.remote.NetworkDataSource
import com.jarroyo.mvimarvelapp.mockCharacterUIModel
import com.jarroyo.mvimarvelapp.mockCharacterUIModelList
import com.jarroyo.mvimarvelapp.mockException
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class DataRepositoryImplTest {

    @MockK
    private lateinit var networkDataSource: NetworkDataSource

    @MockK
    private lateinit var diskDataSource: DiskDataSource

    private lateinit var dataRepository: DataRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataRepository = DataRepositoryImpl(
            networkDataSource,
            diskDataSource,
            Dispatchers.Unconfined
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `GIVEN remote error WHEN call getCharacterList THEN returns EitherLeft`() = runBlocking {
        // Given
        coEvery { networkDataSource.getCharacterList(any()) } returns Result.failure(
            NetworkErrorException()
        )

        // When
        val response = dataRepository.getList(0)

        // Then
        assert(response.isFailure)
    }

    @Test
    fun `GIVEN remote success WHEN call getCharacterList THEN returns EitherRight`() = runBlocking {
        // Given
        coEvery { networkDataSource.getCharacterList(any()) } returns Result.success(
            mockCharacterUIModelList)

        // When
        val response = dataRepository.getList(0)

        // Then
        assert(response.isSuccess)
        assertEquals(response.getOrNull(), mockCharacterUIModelList)
    }

    @Test
    fun `GIVEN remote success WHEN call searchCharacterList THEN returns EitherRight`() = runBlocking {
        // Given
        coEvery { networkDataSource.searchCharacterList(any()) } returns Result.success(
            mockCharacterUIModelList)

        // When
        val flowResponse = dataRepository.search("name")

        // Then
        assert(flowResponse.toList().first().isSuccess)
        assertEquals(flowResponse.toList().first().getOrNull(), mockCharacterUIModelList)
    }
    @Test
    fun `GIVEN remote error WHEN call searchCharacterList THEN returns EitherRight`() = runBlocking {
        // Given
        coEvery { networkDataSource.searchCharacterList(any()) } returns Result.failure(
            mockException)

        // When
        val flowResponse = dataRepository.search("name")

        // Then
        assert(flowResponse.toList().first().isFailure)
    }

    @Test
    fun `GIVEN disk success WHEN call saveFavoriteCharacter THEN returns EitherRight`() = runBlocking {
        // Given
        coEvery { diskDataSource.insertCharacter(any()) } returns Unit

        // When
        val response = dataRepository.saveFavorite(mockCharacterUIModel)

        // Then
        assert(response.isSuccess)
    }
    @Test
    fun `GIVEN disk success WHEN call removeFavoriteCharacter THEN returns EitherRight`() = runBlocking {
        // Given
        coEvery { diskDataSource.removeCharacter(any()) } returns Unit

        // When
        val response = dataRepository.removeFavorite(mockCharacterUIModel)

        // Then
        assert(response.isSuccess)
    }

    @Test
    fun `GIVEN disk success WHEN call getFavoriteCharacter THEN returns EitherRight`() = runBlocking {
        // Given
        coEvery { diskDataSource.getCharacterList() } returns Result.success(
            mockCharacterUIModelList)

        // When
        val response = dataRepository.getFavorite()

        // Then
        assert(response.isSuccess)
        assertEquals(response.getOrNull(), mockCharacterUIModelList)
    }
    @Test
    fun `GIVEN disk error WHEN call getFavoriteCharacter THEN returns EitherLeft`() = runBlocking {
        // Given
        coEvery { diskDataSource.getCharacterList() } returns Result.failure(mockException)

        // When
        val response = dataRepository.getFavorite()

        // Then
        assert(response.isFailure)
    }

    @Test
    fun `GIVEN disk success WHEN call isFavoriteCharacter THEN returns EitherRight`() = runBlocking {
        // Given
        coEvery { diskDataSource.getCharacter(any()) } returns Result.success(mockCharacterUIModel)

        // When
        val response = dataRepository.isFavorite(mockCharacterUIModel)

        // Then
        assert(response)
    }

    @Test
    fun `GIVEN disk error WHEN call isFavoriteCharacter THEN returns EitherLeft`() = runBlocking {
        // Given
        coEvery { diskDataSource.getCharacter(any()) } returns Result.failure(mockException)

        // When
        val response = dataRepository.isFavorite(mockCharacterUIModel)

        // Then
        assert(!response)
    }
}

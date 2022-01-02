package com.jarroyo.mvimarvelapp.data.local

import com.jarroyo.mvimarvelapp.domain.model.toDomain
import com.jarroyo.mvimarvelapp.mockCharacterEntity
import com.jarroyo.mvimarvelapp.mockCharacterEntityList
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class DiskDataSourceImplTest {
    @MockK
    private lateinit var database: AppDatabase

    private lateinit var diskDataSourceImpl: DiskDataSourceImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        diskDataSourceImpl = DiskDataSourceImpl(
            database
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `GIVEN success WHEN call getCharacterList THEN returns EitherRight`() = runBlocking {
        // Given
        coEvery { database.characterDao().getAll()} returns mockCharacterEntityList

        // When
        val response = diskDataSourceImpl.getCharacterList()

        // Then
        assert(response.isSuccess)
        assertEquals(response.getOrNull(), mockCharacterEntityList.toDomain())
    }

    @Test
    fun `GIVEN success WHEN call getCharacter THEN returns EitherRight`() = runBlocking {
        // Given
        coEvery { database.characterDao().getCharacter(any())} returns mockCharacterEntity

        // When
        val response = diskDataSourceImpl.getCharacter(1)

        // Then
        assert(response.isSuccess)
        assertEquals(response.getOrNull(), mockCharacterEntity.toDomain())
    }

}
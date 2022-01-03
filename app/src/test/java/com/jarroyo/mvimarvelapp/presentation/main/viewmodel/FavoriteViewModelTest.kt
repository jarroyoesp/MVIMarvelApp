package com.jarroyo.mvimarvelapp.presentation.main.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jarroyo.mvimarvelapp.domain.interactors.favorite.GetFavoriteListInteractor
import com.jarroyo.mvimarvelapp.mockCharacterUIModelList
import com.jarroyo.mvimarvelapp.mockException
import com.jarroyo.mvimarvelapp.presentation.main.contract.FavoriteContract
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.Assert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoriteViewModelTest {

    @MockK
    private lateinit var getFavoriteListInteractor: GetFavoriteListInteractor

    private lateinit var viewModel: FavoriteViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        MockKAnnotations.init(this)

        viewModel = spyk(
            FavoriteViewModel(
                getFavoriteListInteractor,
            )
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `GIVEN favorites WHEN IntentFetchData is sent THEN getFavoriteListInteractor is called`() =
        runBlockingTest {
            // Given
            coEvery { getFavoriteListInteractor.invoke() } returns Result.success(
                mockCharacterUIModelList
            )

            // When
            viewModel.intents.send(FavoriteContract.Intent.FetchData)

            // Then
            coVerify(exactly = 1) { getFavoriteListInteractor.invoke() }
            val effectList = viewModel.effects.take(3).toList()
            assert(effectList[0] is FavoriteContract.Effect.ShowLoading)
            assert(effectList[1] is FavoriteContract.Effect.HideLoading)
            assert(effectList[2] is FavoriteContract.Effect.ShowFavorites)
            Assert.assertEquals(
                (effectList[2] as FavoriteContract.Effect.ShowFavorites).list,
                mockCharacterUIModelList
            )
        }


    @Test
    fun `GIVEN empty favorites WHEN IntentFetchData is sent THEN getFavoriteListInteractor is called`() =
        runBlockingTest {
            // Given
            coEvery { getFavoriteListInteractor.invoke() } returns Result.success(
                emptyList()
            )

            // When
            viewModel.intents.send(FavoriteContract.Intent.FetchData)

            // Then
            coVerify(exactly = 1) { getFavoriteListInteractor.invoke() }
            val effectList = viewModel.effects.take(3).toList()
            assert(effectList[0] is FavoriteContract.Effect.ShowLoading)
            assert(effectList[1] is FavoriteContract.Effect.HideLoading)
            assert(effectList[2] is FavoriteContract.Effect.NoFavorites)
        }

    @Test
    fun `GIVEN error from favorites WHEN IntentFetchData is sent THEN getFavoriteListInteractor is called`() =
        runBlockingTest {
            // Given
            coEvery { getFavoriteListInteractor.invoke() } returns Result.failure(
                mockException
            )

            // When
            viewModel.intents.send(FavoriteContract.Intent.FetchData)

            // Then
            coVerify(exactly = 1) { getFavoriteListInteractor.invoke() }
            val effectList = viewModel.effects.take(3).toList()
            assert(effectList[0] is FavoriteContract.Effect.ShowLoading)
            assert(effectList[1] is FavoriteContract.Effect.HideLoading)
            assert(effectList[2] is FavoriteContract.Effect.ShowError)
        }

}
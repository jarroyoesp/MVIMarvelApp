package com.jarroyo.mvimarvelapp.presentation.detail.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jarroyo.mvimarvelapp.domain.interactors.favorite.GetFavoriteListInteractor
import com.jarroyo.mvimarvelapp.domain.interactors.favorite.IsFavoriteInteractor
import com.jarroyo.mvimarvelapp.domain.interactors.favorite.RemoveFavoriteInteractor
import com.jarroyo.mvimarvelapp.domain.interactors.favorite.SaveFavoriteInteractor
import com.jarroyo.mvimarvelapp.mockCharacterUIModel
import com.jarroyo.mvimarvelapp.mockCharacterUIModelList
import com.jarroyo.mvimarvelapp.presentation.detail.contract.DetailContract
import com.jarroyo.mvimarvelapp.presentation.main.contract.FavoriteContract
import com.jarroyo.mvimarvelapp.presentation.main.viewmodel.FavoriteViewModel
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
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailViewModelTest {

    @MockK
    private lateinit var isFavoriteInteractor: IsFavoriteInteractor

    @MockK
    private lateinit var saveFavoriteInteractor: SaveFavoriteInteractor

    @MockK
    private lateinit var removeFavoriteInteractor: RemoveFavoriteInteractor

    private lateinit var viewModel: DetailViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        MockKAnnotations.init(this)

        viewModel = spyk(
            DetailViewModel(
                isFavoriteInteractor,
                saveFavoriteInteractor,
                removeFavoriteInteractor,
            )
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `GIVEN isFavorite WHEN IntentIsFavorite is sent THEN isFavoriteInteractor is called`() =
        runBlockingTest {
            // Given
            coEvery { isFavoriteInteractor.invoke(any()) } returns true

            // When
            viewModel.intents.send(DetailContract.Intent.IsFavorite(mockCharacterUIModel))

            // Then
            coVerify(exactly = 1) { isFavoriteInteractor.invoke(any()) }
            val effectList = viewModel.effects.take(1).toList()
            assert(effectList[0] is DetailContract.Effect.ShowIsFavorite)
        }

    @Test
    fun `GIVEN isNoFavorite WHEN IntentIsFavorite is sent THEN isFavoriteInteractor is called`() =
        runBlockingTest {
            // Given
            coEvery { isFavoriteInteractor.invoke(any()) } returns false

            // When
            viewModel.intents.send(DetailContract.Intent.IsFavorite(mockCharacterUIModel))

            // Then
            coVerify(exactly = 1) { isFavoriteInteractor.invoke(any()) }
            val effectList = viewModel.effects.take(1).toList()
            assert(effectList[0] is DetailContract.Effect.ShowIsNoFavorite)
        }

    @Test
    fun `GIVEN noFavorite WHEN SaveFavorite is sent THEN isFavoriteInteractor is called`() =
        runBlockingTest {
            // Given
            coEvery { saveFavoriteInteractor.invoke(any()) } returns Result.success(true)
            viewModel.state.value?.isFavorite  = false

            // When
            viewModel.intents.send(DetailContract.Intent.SaveFavorite(mockCharacterUIModel))

            // Then
            coVerify(exactly = 1) { saveFavoriteInteractor.invoke(any()) }
            val effectList = viewModel.effects.take(1).toList()
            assert(effectList[0] is DetailContract.Effect.ShowIsFavorite)
        }

    @Test
    fun `GIVEN favorite WHEN SaveFavorite is sent THEN isFavoriteInteractor is called`() =
        runBlockingTest {
            // Given
            coEvery { removeFavoriteInteractor.invoke(any()) } returns Result.success(true)
            viewModel.state.value?.isFavorite  = true

            // When
            viewModel.intents.send(DetailContract.Intent.SaveFavorite(mockCharacterUIModel))

            // Then
            coVerify(exactly = 1) { removeFavoriteInteractor.invoke(any()) }
            val effectList = viewModel.effects.take(1).toList()
            assert(effectList[0] is DetailContract.Effect.ShowIsNoFavorite)
        }
}
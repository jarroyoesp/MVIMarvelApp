package com.jarroyo.mvimarvelapp.presentation.main.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jarroyo.mvimarvelapp.domain.interactors.GetListInteractor
import com.jarroyo.mvimarvelapp.domain.interactors.SearchInteractor
import com.jarroyo.mvimarvelapp.mockCharacterUIModelList
import com.jarroyo.mvimarvelapp.mockException
import com.jarroyo.mvimarvelapp.presentation.main.contract.MainContract
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import junit.framework.Assert.assertEquals
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

class MainViewModelTest {

    @MockK
    private lateinit var getListInteractor: GetListInteractor

    @MockK
    private lateinit var searchInteractor: SearchInteractor

    private lateinit var viewModel: MainViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        MockKAnnotations.init(this)

        MainViewModel.DEBOUNCE = 0L
        viewModel = spyk(
            MainViewModel(
                getListInteractor,
                searchInteractor
            )
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `GIVEN success from getListInteractor WHEN IntentFetchData is sent THEN getListInteractor is called`() =
        runBlockingTest {
            // Given
            coEvery { getListInteractor.invoke(any()) } returns Result.success(
                mockCharacterUIModelList
            )

            // When
            viewModel.intents.send(MainContract.Intent.OnViewAttached)

            // Then
            coVerify(exactly = 1) { getListInteractor.invoke(any()) }
            val effectList = viewModel.effects.take(2).toList()
            assert(effectList[0] is MainContract.Effect.HideLoading)
            assert(effectList[1] is MainContract.Effect.ShowPage)
            assertEquals(
                (effectList[1] as MainContract.Effect.ShowPage).list,
                mockCharacterUIModelList
            )
        }

    @Test
    fun `GIVEN error from getListInteractor WHEN IntentFetchData is sent THEN getListInteractor is called`() =
        runBlockingTest {
            // Given
            coEvery { getListInteractor.invoke(any()) } returns Result.failure(
                mockException
            )

            // When
            viewModel.intents.send(MainContract.Intent.OnViewAttached)

            // Then
            coVerify(exactly = 1) { getListInteractor.invoke(any()) }
            val effectList = viewModel.effects.take(2).toList()
            assert(effectList[0] is MainContract.Effect.HideLoading)
            assert(effectList[1] is MainContract.Effect.ShowError)
            assertEquals(
                (effectList[1] as MainContract.Effect.ShowError).message,
                mockException.message
            )
        }

    @Test
    fun `GIVEN success from search WHEN IntentSearchData is sent THEN searchInteractor is called`() =
        runBlockingTest {
            // Given
            coEvery { searchInteractor.invoke(any()) } returns Result.success(
                mockCharacterUIModelList
            )

            // When
            viewModel.intents.send(MainContract.Intent.OnSearchCharacter("asdf"))

            // Then
            coVerify(exactly = 1) { searchInteractor.invoke(any()) }
            val effectList = viewModel.effects.take(3).toList()
            assert(effectList[0] is MainContract.Effect.ShowLoading)
            assert(effectList[1] is MainContract.Effect.HideLoading)
            assert(effectList[2] is MainContract.Effect.ShowSearch)
            assertEquals(
                (effectList[2] as MainContract.Effect.ShowSearch).list,
                mockCharacterUIModelList
            )
        }

    @Test
    fun `GIVEN error from search WHEN IntentSearchData is sent THEN searchInteractor is called`() =
        runBlockingTest {
            // Given
            coEvery { searchInteractor.invoke(any()) } returns Result.failure(
                mockException
            )

            // When
            viewModel.intents.send(MainContract.Intent.OnSearchCharacter("asdf"))

            // Then
            coVerify(exactly = 1) { searchInteractor.invoke(any()) }
            val effectList = viewModel.effects.take(3).toList()
            assert(effectList[0] is MainContract.Effect.ShowLoading)
            assert(effectList[1] is MainContract.Effect.HideLoading)
            assert(effectList[2] is MainContract.Effect.InitialState)
        }

    @Test
    fun `GIVEN emptydata from search WHEN IntentSearchData is sent THEN searchInteractor is called`() =
        runBlockingTest {
            // Given
            coEvery { searchInteractor.invoke(any()) } returns Result.failure(
                mockException
            )

            // When
            viewModel.intents.send(MainContract.Intent.OnSearchCharacter("asdf"))

            // Then
            coVerify(exactly = 1) { searchInteractor.invoke(any()) }
            val effectList = viewModel.effects.take(3).toList()
            assert(effectList[0] is MainContract.Effect.ShowLoading)
            assert(effectList[1] is MainContract.Effect.HideLoading)
            assert(effectList[2] is MainContract.Effect.InitialState)
        }
}

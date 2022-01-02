package com.jarroyo.mvimarvelapp.presentation.main.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.jarroyo.mvimarvelapp.R
import com.jarroyo.mvimarvelapp.databinding.FragmentFavoriteBinding
import com.jarroyo.mvimarvelapp.domain.model.UiModel
import com.jarroyo.mvimarvelapp.presentation.main.adapter.ListRVAdapter
import com.jarroyo.mvimarvelapp.presentation.main.contract.FavoriteContract
import com.jarroyo.mvimarvelapp.presentation.main.contract.MainContract
import com.jarroyo.mvimarvelapp.presentation.main.viewmodel.FavoriteViewModel
import com.jarroyo.mvimarvelapp.presentation.utils.IView
import com.jarroyo.mvimarvelapp.presentation.utils.addGridSeparators
import com.jarroyo.mvimarvelapp.presentation.utils.gone
import com.jarroyo.mvimarvelapp.presentation.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment(), IView<FavoriteContract.Effect> {

    companion object {
        private val TAG = FavoriteFragment::class.java.simpleName
        fun newInstance() = FavoriteFragment()
    }

    private lateinit var binding: FragmentFavoriteBinding

    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var adapter: ListRVAdapter

    private lateinit var listener: OnCharacterFavoriteListListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCharacterFavoriteListListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnCharacterFavoriteListListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        initView()
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    /**
     * Initialize ViewModel Observers
     */
    private fun observeState() {
        lifecycleScope.launch {
            viewModel.effects.onEach { effect ->
                render(effect)
            }.collect()
        }
    }

    private fun initView() {
        initRecyclerView()
    }

    private fun showNoContent() {
        binding.fragmentFavoriteListRv.gone()
        binding.fragmentFavoriteLayoutNoContent.layoutNoContentMain.visible()
        binding.fragmentFavoriteLayoutNoContent.layoutNoContentTv.text =
            getString(R.string.no_content_favorite)
    }

    private fun showError(throwable: Throwable) {
        Log.d(TAG, "[showError]")
    }

    private fun showList(list: List<UiModel>) {
        Log.d(TAG, "[showList] $list")
        adapter.showList(list)
    }

    private fun showLoading() {
        Log.d(TAG, "[showLoading]")
    }

    private fun hideLoading() {
        Log.d(TAG, "[hideLoading]")
    }

    private fun initRecyclerView() {
        val layoutManager = GridLayoutManager(context, 2)
        binding.fragmentFavoriteListRv.layoutManager = layoutManager

        adapter = ListRVAdapter(onClickItemListener = {
            listener.onClickCharacterFavorite(it.itemView, it.uiModel)
        })
        binding.fragmentFavoriteListRv.adapter = adapter

        binding.fragmentFavoriteListRv.addGridSeparators()
    }

    interface OnCharacterFavoriteListListener {
        fun onClickCharacterFavorite(view: View, UiModel: UiModel)
    }


    private fun getData() {
        // Fetching data when the application launched
        lifecycleScope.launch {
            viewModel.intents.send(FavoriteContract.Intent.FetchData)
        }
    }

    override fun render(effect: FavoriteContract.Effect) {
        Log.d(TAG, "[render] $effect")
        when (effect) {
            FavoriteContract.Effect.HideLoading -> {
                hideLoading()
            }
            FavoriteContract.Effect.InitialState -> {

            }
            FavoriteContract.Effect.NoFavorites -> {
                showNoContent()
            }
            is FavoriteContract.Effect.ShowError -> {
                showError(effect.throwable)
            }
            is FavoriteContract.Effect.ShowFavorites -> {
                showList(effect.list)
            }
            FavoriteContract.Effect.ShowLoading -> {
                showLoading()
            }
        }
    }
}
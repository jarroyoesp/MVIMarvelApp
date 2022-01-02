package com.jarroyo.mvimarvelapp.presentation.detail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jarroyo.mvimarvelapp.R
import com.jarroyo.mvimarvelapp.databinding.FragmentDetailBinding
import com.jarroyo.mvimarvelapp.domain.model.UiModel
import com.jarroyo.mvimarvelapp.presentation.detail.adapter.ListItemRVAdapter
import com.jarroyo.mvimarvelapp.presentation.detail.contract.DetailContract
import com.jarroyo.mvimarvelapp.presentation.detail.viewmodel.DetailViewModel
import com.jarroyo.mvimarvelapp.presentation.utils.IView
import com.jarroyo.mvimarvelapp.presentation.utils.gone
import com.jarroyo.mvimarvelapp.presentation.utils.loadUrl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private const val ARG_CHARACTER = "ARG_CHARACTER"

@AndroidEntryPoint
class DetailFragment : Fragment(), IView<DetailContract.Effect> {
    private lateinit var uiModel: UiModel

    private val viewModel: DetailViewModel by viewModels()

    private lateinit var binding: FragmentDetailBinding

    private lateinit var adapterComics: ListItemRVAdapter
    private lateinit var adapterSeries: ListItemRVAdapter
    private lateinit var adapterStories: ListItemRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            uiModel = it!!.getParcelable(ARG_CHARACTER)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setTransitionName(
            binding.fragmentDetailToolbar.toolbarCollapsableImageHeader,
            "hero_image"
        )
        initView()
        observeState()
        uiModel?.let {
            getData()
        }
    }

    private fun getData() {
        // Fetching data when the application launched
        lifecycleScope.launch {
            viewModel.intents.send(DetailContract.Intent.IsFavorite(uiModel = uiModel))
        }
    }

    private fun saveFavorite() {
        // Fetching data when the application launched
        lifecycleScope.launch {
            viewModel.intents.send(DetailContract.Intent.SaveFavorite(uiModel = uiModel))
        }
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

    private fun showFavoriteState(isFavorite: Boolean) {
        if (isFavorite) {
            binding.fragmentDetailFavButton.setImageResource(R.drawable.ic_favorite)
        } else {
            binding.fragmentDetailFavButton.setImageResource(R.drawable.ic_favorite_unselected)
        }
    }

    private fun showStateAfterAction(isFavorite: Boolean) {
        if (isFavorite) {
            showSnackBar(getString(R.string.add_to_favorites))
        } else {
            showSnackBar(getString(R.string.remove_from_favorites))
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
            .show()
    }

    private fun initView() {
        configActionBar()
        configRecyclerviewComics()
        configRecyclerviewSeries()
        configRecyclerviewStories()
        binding.fragmentDetailFavButton.setOnClickListener {
            uiModel?.let {
                saveFavorite()
            }
        }

        binding.fragmentDetailTvDescription.text = uiModel?.description
        binding.fragmentDetailToolbar.toolbarCollapsableTvTitle.title = uiModel?.name
        binding.fragmentDetailToolbar.toolbarCollapsableImageHeader.loadUrl(
            requireContext(),
            uiModel?.imageHomeUrl
        )
    }

    private fun configRecyclerviewComics() {
        uiModel?.comicList?.let {
            if (it.isEmpty()) {
                binding.fragmentCharacterDetailLayoutComics.gone()
            } else {
                val layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                binding.fragmentCharacterDetailComicsRv.layoutManager = layoutManager

                adapterComics = ListItemRVAdapter(onClickItemListener = {

                })
                binding.fragmentCharacterDetailComicsRv.adapter = adapterComics

                adapterComics.showList(it)
            }
        } ?: binding.fragmentCharacterDetailLayoutComics.gone()
    }

    private fun configRecyclerviewSeries() {
        uiModel?.seriesList?.let {
            if (it.isEmpty()) {
                binding.fragmentCharacterDetailLayoutSeries.gone()
            } else {
                val layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                binding.fragmentCharacterDetailSeriesRv.layoutManager = layoutManager

                adapterSeries = ListItemRVAdapter(onClickItemListener = {

                })
                binding.fragmentCharacterDetailSeriesRv.adapter = adapterSeries

                adapterSeries.showList(it)
            }
        } ?: binding.fragmentCharacterDetailLayoutSeries.gone()
    }

    private fun configRecyclerviewStories() {
        uiModel?.storiesList?.let {
            if (it.isEmpty()) {
                binding.fragmentCharacterDetailLayoutStories.gone()
            } else {
                val layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                binding.fragmentCharacterDetailStoriesRv.layoutManager = layoutManager

                adapterStories = ListItemRVAdapter(onClickItemListener = {

                })
                binding.fragmentCharacterDetailStoriesRv.adapter = adapterStories

                adapterStories.showList(it)
            }
        } ?: binding.fragmentCharacterDetailLayoutStories.gone()
    }

    private fun configActionBar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.fragmentDetailToolbar.toolbarCollapsableTvTitle)
        (activity as AppCompatActivity).supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId === android.R.id.home) {
            activity?.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        @JvmStatic
        fun newInstance(uiModel: UiModel) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_CHARACTER, uiModel)
                }
            }
    }

    override fun render(effect: DetailContract.Effect) {
        when(effect) {
            DetailContract.Effect.HideLoading -> TODO()
            DetailContract.Effect.InitialState -> TODO()
            is DetailContract.Effect.ShowError -> TODO()
            DetailContract.Effect.ShowIsFavorite -> showFavoriteState(true)
            DetailContract.Effect.ShowIsNoFavorite -> showFavoriteState(false)
            DetailContract.Effect.ShowLoading -> TODO()
            is DetailContract.Effect.ShowSnackBar -> {
                showStateAfterAction(effect.isFavorite)
            }
        }
    }
}
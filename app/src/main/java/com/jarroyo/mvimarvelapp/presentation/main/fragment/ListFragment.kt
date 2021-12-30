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
import androidx.recyclerview.widget.RecyclerView
import com.jarroyo.mvimarvelapp.databinding.FragmentListBinding
import com.jarroyo.mvimarvelapp.domain.model.UiModel
import com.jarroyo.mvimarvelapp.presentation.main.adapter.ListRVAdapter
import com.jarroyo.mvimarvelapp.presentation.main.contract.MainContract
import com.jarroyo.mvimarvelapp.presentation.main.viewmodel.MainViewModel
import com.jarroyo.mvimarvelapp.presentation.utils.IView
import com.jarroyo.mvimarvelapp.presentation.utils.addGridSeparators
import com.jarroyo.mvimarvelapp.presentation.utils.gone
import com.jarroyo.mvimarvelapp.presentation.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ListFragment : Fragment(), IView<MainContract.Effect> {

    companion object {
        private val TAG = ListFragment::class.java.simpleName
        private const val DEBOUNCE_SEARCH = 600L

        @JvmStatic
        fun newInstance() = ListFragment()
    }

    private lateinit var binding: FragmentListBinding

    private val viewModel: MainViewModel by viewModels()

    private lateinit var adapter: ListRVAdapter

    private lateinit var listener: OnCharacterListListener

    private var isUserScroll = false
    private var isUserSearching = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCharacterListListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnCharacterListListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        initView()
        getData()
    }

    private fun initView() {
        binding.fragmentCharacterListLayoutErrorButton.setOnClickListener { getData() }
        binding.fragmentCharacterListLayoutEmpty.visible()
        initRecyclerView()
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

    private fun initRecyclerView() {
        binding.fragmentCharacterListSwipeRefreshlayout.setOnRefreshListener {
            if (!isUserSearching) {
                getData()
            }
        }

        val layoutManager = GridLayoutManager(context, 2)
        binding.fragmentCharacterListRv.layoutManager = layoutManager

        adapter = ListRVAdapter(onClickItemListener = {
            listener.onClickCharacter(it.itemView, it.uiModel)
        })
        binding.fragmentCharacterListRv.adapter = adapter

        binding.fragmentCharacterListRv.addGridSeparators()
        recyclerViewListener()
    }

    /**
     * RecyclerView Listener to detect user scrolls to bottom
     */
    private fun recyclerViewListener() {
        binding.fragmentCharacterListRv.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        if (isUserScroll && !isUserSearching && !recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                            isUserScroll = false
                            Log.d(TAG, "[onScrollStateChanged] end")
                            getData()
                        }

                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Log.d(TAG, "[onScrolled]")
                super.onScrolled(recyclerView, dx, dy)
                isUserScroll = true
            }
        })
    }

    private fun getData() {
        // Fetching data when the application launched
        lifecycleScope.launch {
            viewModel.intents.send(MainContract.Intent.FetchData)
        }
    }

    private fun showError(message: String) {
        Log.d(TAG, "[showError]")
        binding.fragmentCharacterListLayoutError.visible()
        binding.fragmentCharacterListRv.gone()
        binding.fragmentCharacterListLayoutEmpty.gone()
    }

    private fun showList(list: List<UiModel>?) {
        Log.d(TAG, "[showList] $list")
        binding.fragmentCharacterListLayoutEmpty.gone()
        binding.fragmentCharacterListRv.visible()
        adapter.updateList(list!!)
    }

    private fun showResults(list: List<UiModel>) {
        Log.d(TAG, "[showList] $list")
        binding.fragmentCharacterListLayoutEmpty.gone()
        binding.fragmentCharacterListRv.visible()
        adapter.showList(list)
    }


    private fun showLoading() {
        Log.d(TAG, "[showLoading]")
        binding.fragmentCharacterListSwipeRefreshlayout.isRefreshing = true
        binding.fragmentCharacterListLayoutError.gone()
    }

    private fun hideLoading() {
        Log.d(TAG, "[hideLoading]")
        binding.fragmentCharacterListSwipeRefreshlayout.isRefreshing = false
    }

    interface OnCharacterListListener {
        fun onClickCharacter(view: View, uiModel: UiModel)
    }

    override fun render(effect: MainContract.Effect) {
        Log.d(TAG, "[render] $effect")
        when (effect) {
            MainContract.Effect.ShowLoading -> {
                showLoading()
            }
            MainContract.Effect.HideLoading -> {
                hideLoading()
            }
            is MainContract.Effect.ShowList -> {
                showList(effect.list)
            }
            is MainContract.Effect.ShowError -> {
                showError(effect.message)
            }
            MainContract.Effect.InitialState -> {//TODO()
             }
        }
    }
}
package com.jarroyo.mvimarvelapp.presentation.main.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.jarroyo.mvimarvelapp.R
import com.jarroyo.mvimarvelapp.presentation.main.contract.MainContract
import com.jarroyo.mvimarvelapp.presentation.main.viewmodel.MainViewModel
import com.jarroyo.mvimarvelapp.presentation.utils.IView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), IView<MainContract.Effect> {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observeState()
        getData()
    }
    private fun observeState() {
        viewModel.effects.observe(this, Observer {
            render(it)
        })
    }

    private fun getData() {
        // Fetching data when the application launched
        lifecycleScope.launch {
            viewModel.intents.send(MainContract.Intent.FetchData)
        }
    }

    override fun render(effect: MainContract.Effect) {
        when(effect) {
            MainContract.Effect.ShowLoading -> {}
            MainContract.Effect.HideLoading -> {}
            is MainContract.Effect.ShowList -> {Toast.makeText(this, "${effect.list}", Toast.LENGTH_SHORT).show()}
            is MainContract.Effect.ShowError -> {Toast.makeText(this, "${effect.message}", Toast.LENGTH_SHORT).show()}
        }
    }
}
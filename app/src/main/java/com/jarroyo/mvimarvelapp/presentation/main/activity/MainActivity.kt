package com.jarroyo.mvimarvelapp.presentation.main.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jarroyo.mvimarvelapp.R
import com.jarroyo.mvimarvelapp.databinding.ActivityMainBinding
import com.jarroyo.mvimarvelapp.domain.model.UiModel
import com.jarroyo.mvimarvelapp.presentation.main.fragment.ListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ListFragment.OnCharacterListListener {
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initView()
    }

    private fun initView() {
        Log.d(TAG, "[initView]")
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_character_list, R.id.navigation_favorite
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onClickCharacter(view: View, uiModel: UiModel) {
        // TODO
    }
/*
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
    }*/
}
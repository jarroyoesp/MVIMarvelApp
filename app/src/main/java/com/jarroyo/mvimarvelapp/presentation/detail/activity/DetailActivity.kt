package com.jarroyo.mvimarvelapp.presentation.detail.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jarroyo.mvimarvelapp.R
import com.jarroyo.mvimarvelapp.databinding.ActivityDetailBinding
import com.jarroyo.mvimarvelapp.domain.model.UiModel
import com.jarroyo.mvimarvelapp.presentation.detail.fragment.DetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    companion object {

        const val ARG_CHARACTER = "ARG_CHARACTER"
    }

    private lateinit var binding: ActivityDetailBinding

    private lateinit var uiModel: UiModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        getExtras()
        addFragment(uiModel)
    }

    private fun getExtras() {
        uiModel  = intent.extras!!.get(ARG_CHARACTER) as UiModel
    }

    private fun addFragment(uiModel: UiModel) {
        val newFragment = DetailFragment.newInstance(uiModel)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.activity_detail_layout_main, newFragment)
        transaction.commit()
    }
}
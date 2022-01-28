package com.jarroyo.mvimarvelapp.presentation.main.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jarroyo.mvimarvelapp.R
import com.jarroyo.mvimarvelapp.domain.model.UiModel
import com.jarroyo.mvimarvelapp.presentation.utils.loadUrl
import timber.log.Timber

class ListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private val textViewTitle: TextView = view.findViewById(R.id.item_rv_textview_title)
    private val imageView: ImageView = view.findViewById(R.id.item_rv_imageView)
    private var layoutMain: ViewGroup = view.findViewById(R.id.item_rv_layout_main)

    fun bind(position: Int, uiModel: UiModel, listener: (ListRVAdapter.ItemCharacter) -> Unit) {
        Timber.d("$uiModel")
        textViewTitle.text = "${uiModel.name}"

        imageView.loadUrl(view.context, uiModel.imageHomeUrl)

        layoutMain.setOnClickListener {
            listener(ListRVAdapter.ItemCharacter(position, uiModel, view))
        }
    }
}

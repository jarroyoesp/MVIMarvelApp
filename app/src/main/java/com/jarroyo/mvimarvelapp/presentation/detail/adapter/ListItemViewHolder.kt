package com.jarroyo.mvimarvelapp.presentation.detail.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jarroyo.mvimarvelapp.R
import com.jarroyo.mvimarvelapp.domain.model.ItemUIModel

class ListItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        private val TAG = ListItemViewHolder.javaClass.simpleName
    }

    private val textViewTitle: TextView = view.findViewById(R.id.item_rv_textview_title)
    private var layoutMain: ViewGroup = view.findViewById(R.id.item_rv_layout_main)

    fun bind(
        position: Int,
        itemUIModel: ItemUIModel,
        listener: (ListItemRVAdapter.ItemData) -> Unit
    ) {
        Log.d(TAG, "[bind] $itemUIModel")
        textViewTitle.text = "${itemUIModel.title}"

        // imageView.loadUrl(view.context, characterUIModel.imageHomeUrl)

        layoutMain.setOnClickListener {
            listener(ListItemRVAdapter.ItemData(position, itemUIModel, view))
        }
    }
}

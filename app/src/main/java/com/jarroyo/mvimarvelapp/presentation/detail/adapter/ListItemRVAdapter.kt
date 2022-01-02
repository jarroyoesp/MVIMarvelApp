package com.jarroyo.mvimarvelapp.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jarroyo.mvimarvelapp.R
import com.jarroyo.mvimarvelapp.domain.model.ItemUIModel
import com.jarroyo.mvimarvelapp.presentation.utils.notifyChanges
import kotlin.properties.Delegates

class ListItemRVAdapter(
    private var onClickItemListener: (ItemData) -> Unit
) : RecyclerView.Adapter<ListItemViewHolder>() {

    private var dataSet: List<ItemUIModel> by Delegates.observable(emptyList()) { _, oldList, newList ->
        notifyChanges(oldList, newList) { o, n -> o.resourceURI == n.resourceURI }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListItemViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_rv_detail, viewGroup, false)

        return ListItemViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ListItemViewHolder, position: Int) {
        viewHolder.bind(position, dataSet[position], onClickItemListener)
    }

    override fun getItemCount() = dataSet.size

    fun showList(list: List<ItemUIModel>) {
        dataSet = list
    }

    data class ItemData(val position: Int, val itemUIModel: ItemUIModel, val itemView: View)
}
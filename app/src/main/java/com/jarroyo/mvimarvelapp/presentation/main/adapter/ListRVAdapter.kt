package com.jarroyo.mvimarvelapp.presentation.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jarroyo.mvimarvelapp.R
import com.jarroyo.mvimarvelapp.domain.model.UiModel
import com.jarroyo.mvimarvelapp.presentation.utils.notifyChanges
import kotlin.properties.Delegates

class ListRVAdapter(
    private var onClickItemListener: (ItemCharacter) -> Unit
) : RecyclerView.Adapter<ListViewHolder>() {

    private var dataSet: List<UiModel> by Delegates.observable(emptyList()) { _, oldList, newList ->
        notifyChanges(oldList, newList) { o, n -> o.id == n.id }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_rv, viewGroup, false)

        return ListViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ListViewHolder, position: Int) {
        viewHolder.bind(position, dataSet[position], onClickItemListener)
    }

    override fun getItemCount() = dataSet.size

    fun updateList(list: List<UiModel>) {
        val auxList = dataSet.toMutableList()
        auxList.addAll(list)
        dataSet = auxList
    }

    fun showList(list: List<UiModel>) {
        dataSet = list
    }

    data class ItemCharacter(val position: Int, val uiModel: UiModel, val itemView: View)
}

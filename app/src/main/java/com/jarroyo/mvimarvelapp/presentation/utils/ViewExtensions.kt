package com.jarroyo.mvimarvelapp.presentation.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.jarroyo.mvimarvelapp.R
import com.squareup.picasso.Picasso

fun ImageView.loadUrl(context: Context, url: String?) {
    url?.let {
        Picasso.with(context).load(url).into(this)
    }
}

fun ViewGroup.inflate(
    inflater: LayoutInflater,
    @LayoutRes layoutRes: Int,
    attached: Boolean = false
): View = inflater.inflate(layoutRes, this, attached)

fun <T : View> T?.visible(): T? = this?.apply { visibility = View.VISIBLE }

fun <T : View> T?.invisible(): T? = this?.apply { visibility = View.INVISIBLE }

fun <T : View> T?.gone(): T? = this?.apply { visibility = View.GONE }

fun View.isVisible() = this.visibility == View.VISIBLE
fun View.isGone() = this.visibility == View.GONE

fun <T> RecyclerView.Adapter<*>.notifyChanges(
    oldList: List<T>,
    newList: List<T>,
    compare: (T, T) -> Boolean
) {

    val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            compare(oldList[oldItemPosition], newList[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]

        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
    })
    diff.dispatchUpdatesTo(this)
}

fun RecyclerView.addGridSeparators() {
    ContextCompat.getDrawable(
        this.context,
        R.drawable.divider_rv_transparent
    )?.let {
        // Separator
        val itemDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(
            it
        )
        val itemDecorationHorizontal =
            DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL)
        itemDecorationHorizontal.setDrawable(
            it
        )
        this.addItemDecoration(itemDecoration)
        this.addItemDecoration(itemDecorationHorizontal)
    }
}

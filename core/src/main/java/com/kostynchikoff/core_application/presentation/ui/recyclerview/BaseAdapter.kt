package com.kostynchikoff.core_application.presentation.ui.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseViewHolder<T>>() {

    internal var items = mutableListOf<T>()

    private var onItemClickListener: (any: Any) -> Unit = {}

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun inflate(parent: ViewGroup, @LayoutRes layoutId: Int) : View {
        return LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
    }

    fun setOnRvClickListener(onItemClickListener: (any: Any) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    fun onItemClick(any: Any) {
        onItemClickListener.invoke(any)
    }

    fun setItems(list: List<T>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun getItems() : List<T> = items

}
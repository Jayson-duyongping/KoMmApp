package com.jayson.komm.ui.viewpager2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jayson.komm.ui.R

class PageAdapter(private val data: MutableList<String>) :
    RecyclerView.Adapter<PageAdapter.ViewHolder>() {

    // 创建 ViewHolder
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // 在这里绑定视图元素
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 创建 ViewHolder，并设置布局
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        // 在这里将数据绑定到 ViewHolder 中的视图元素
    }

    override fun getItemCount() = data.size

    fun addItem(item: String, position: Int) {
        data.add(position, item)
        notifyItemInserted(position)
    }

    fun removeItem(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }
}
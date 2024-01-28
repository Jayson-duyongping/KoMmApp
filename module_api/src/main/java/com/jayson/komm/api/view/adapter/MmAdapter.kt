package com.jayson.komm.api.view.adapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jayson.komm.api.R
import com.jayson.komm.api.bean.Mm

/**
 * @Author: Jayson
 * @CreateDate: 2023/4/3 20:43
 * @Version: 1.0
 * @Description:
 */
class MmAdapter(
    private val context: Context,
    private val column: Int = 2
) : RecyclerView.Adapter<MmAdapter.ImageViewHolder>() {

    private var dataItems: MutableList<Mm> = mutableListOf()

    // 用于下拉刷新的刷新状态，初始值为false
    var isRefreshing = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val layoutParams = holder.itemView.layoutParams
        layoutParams.width = imageWidth()
        holder.itemView.layoutParams = layoutParams
        Glide.with(context)
            .load(dataItems[position].imageUrl)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return dataItems.size
    }

    fun setData(data: MutableList<Mm>) {
        dataItems = data
        notifyDataSetChanged()
    }

    fun addData(data: MutableList<Mm>) {
        val startPosition = dataItems.size
        dataItems.addAll(data)
        notifyItemRangeInserted(startPosition, data.size)
    }


    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
    }

    private fun imageWidth(): Int {
        val count = if (column < 2) 2 else column
        return Resources.getSystem().displayMetrics.widthPixels / count
    }
}
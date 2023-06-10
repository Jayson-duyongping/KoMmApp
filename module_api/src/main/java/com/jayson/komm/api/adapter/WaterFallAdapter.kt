package com.jayson.komm.api.adapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jayson.komm.api.R
import com.jayson.komm.api.bean.Picture

/**
 * @Author: Jayson
 * @CreateDate: 2023/4/3 20:43
 * @Version: 1.0
 * @Description:
 */
class WaterFallAdapter(
    private val context: Context,
    private val column: Int = 2
) : PagingDataAdapter<Picture, WaterFallAdapter.ImageViewHolder>(diffCallback) {

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Picture>() {
            override fun areItemsTheSame(oldItem: Picture, newItem: Picture): Boolean {
                return oldItem.hoverUrl == newItem.hoverUrl
            }

            override fun areContentsTheSame(oldItem: Picture, newItem: Picture): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val layoutParams = holder.itemView.layoutParams
        layoutParams.width = imageWidth()
        holder.itemView.layoutParams = layoutParams
        val data = getItem(position)
        holder.bind(data)
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.image_view)
        fun bind(repo: Picture?) {
            Glide.with(context)
                .load(repo?.hoverUrl)
                .into(imageView)
        }
    }

    private fun imageWidth(): Int {
        val count = if (column < 2) 2 else column
        return Resources.getSystem().displayMetrics.widthPixels / count
    }
}
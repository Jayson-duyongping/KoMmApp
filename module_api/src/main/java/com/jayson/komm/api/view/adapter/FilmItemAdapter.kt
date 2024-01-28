package com.jayson.komm.api.view.adapter

import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jayson.komm.api.bean.Film
import com.jayson.komm.api.databinding.ItemFilmBinding
import com.jayson.komm.common.ext.addClickScale

class FilmItemAdapter : ListAdapter<Film, FilmItemAdapter.ItemViewHolder>(ItemDiffCallback()) {

    private var onItemClickListener: OnItemClickListener? = null
    private var isExpanded = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFilmBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        // 绑定数据
        holder.bind(item, position)

        // 设置点击事件监听器
        holder.itemView.apply {
            addClickScale {}
            setOnClickListener {
                onItemClickListener?.onItemClick(item, position)
            }
        }
    }

    inner class ItemViewHolder(private val binding: ItemFilmBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Film, position: Int) {
            binding.film = item
            binding.position = position + 1
            binding.executePendingBindings()
            item.data?.get(0)?.poster?.let {
                Glide.with(binding.root.context).load(it).into(binding.posterIv)
            }
            item.data?.get(0)?.description?.let { description ->
                binding.plotTv.apply {
                    val collapsedText = getCollapsedText(this, description)
                    text = collapsedText
                    movementMethod = LinkMovementMethod.getInstance()
                    setOnClickListener {
                        isExpanded = !isExpanded
                        text = if (isExpanded) description else collapsedText
                    }
                }
            }
        }
    }

    class ItemDiffCallback : DiffUtil.ItemCallback<Film>() {
        override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean {
            return oldItem == newItem
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(item: Film, position: Int)
    }

    private fun getCollapsedText(textView: TextView, longText: String): CharSequence {
        val collapsedText = SpannableString(longText.substring(0, 10) + "...")
        collapsedText.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                isExpanded = !isExpanded
                textView.text = if (isExpanded) longText else collapsedText
            }
        }, 13, 16, 0)
        return collapsedText
    }
}

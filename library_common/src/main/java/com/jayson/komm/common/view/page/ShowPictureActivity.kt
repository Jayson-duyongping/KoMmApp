package com.jayson.komm.common.view.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.jayson.komm.common.R
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.bean.FileInfo
import com.jayson.komm.common.databinding.ActivityShowPictureBinding
import com.jayson.komm.common.ext.context
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf

@OptIn(ExperimentalSerializationApi::class)
class ShowPictureActivity : BaseActivity() {

    private lateinit var binding: ActivityShowPictureBinding

    private var files = listOf<FileInfo>()

    override fun initView() {
        super.initView()
        binding = ActivityShowPictureBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewPager()
    }

    override fun initData() {
        super.initData()
        val byteArray = intent.getByteArrayExtra("pictureList")
        byteArray?.let {
            files = ProtoBuf.decodeFromByteArray<ArrayList<FileInfo>>(it)
        }
        val position = intent?.getIntExtra("position", 0) ?: 0
        binding.pictureVp.setCurrentItem(position, false)
    }

    private fun initViewPager() {
        // 设置ViewPager2的适配器
        binding.pictureVp.apply {
            adapter = PictureAdapter()
            // 设置ViewPager2为水平方向可滑动
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            // 预加载3
            offscreenPageLimit = 5
        }
    }

    inner class PictureAdapter : RecyclerView.Adapter<PictureAdapter.PictureViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.layout_photo, parent, false)
            return PictureViewHolder(view)
        }

        override fun onBindViewHolder(holder: PictureAdapter.PictureViewHolder, position: Int) {
            val file = files[position]
            holder.bind(file)
        }

        override fun getItemCount(): Int {
            return files.size
        }

        inner class PictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val photoView = itemView.findViewById<ImageView>(R.id.photoView)
            fun bind(item: FileInfo) {
                Glide.with(context)
                    .load(item.path)
                    .into(photoView)
            }
        }
    }
}
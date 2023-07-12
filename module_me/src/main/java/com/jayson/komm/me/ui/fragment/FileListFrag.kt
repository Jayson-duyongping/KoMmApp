package com.jayson.komm.me.ui.fragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jayson.komm.common.base.BaseListFragment
import com.jayson.komm.common.bean.FileInfo
import com.jayson.komm.common.view.page.ShowPictureActivity
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf

@OptIn(ExperimentalSerializationApi::class)
class FileListFrag : BaseListFragment<FileInfo>() {

    companion object {
        private const val TAG = "FileListFrag"
    }

    private var fileList = arrayListOf<FileInfo>()

    override fun setupInit() {
        setRefreshOrLoadEnable(
            refreshEnable = false,
            loadEnable = false,
            layoutManager = GridLayoutManager(context, 4)
        )
    }

    /**
     * 刷新数据
     */
    fun refreshData(files: ArrayList<FileInfo>) {
        fileList = files
        refreshData()
    }

    override fun getListData(): List<FileInfo> {
        // 这里是获取列表数据的逻辑
        return fileList
    }

    override fun noData(refreshOrLoad: Int) {
    }

    override fun createDataViewHolder(parent: ViewGroup): DataViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(com.jayson.komm.common.R.layout.item_picture, parent, false)
        return DataViewHolder(view)
    }

    override fun bindDataViewHolder(
        holder: RecyclerView.ViewHolder,
        data: FileInfo?,
        position: Int
    ) {
        if (holder is DataViewHolder) {
            context?.let {
                data?.let { info ->
                    Glide.with(it)
                        .load(info.path)
                        .placeholder(com.jayson.komm.res.R.mipmap.placeholder)
                        .into(holder.imgIv)
                    holder.imgIv.setOnClickListener {
                        val intent = Intent(activity, ShowPictureActivity::class.java).apply {
                            putExtra("position", position)
                            // 将其序列化为字节数组,将字节数组添加到Intent中
                            val fileList = ProtoBuf.encodeToByteArray(fileList)
                            putExtra("pictureList", fileList)
                        }
                        startActivity(intent)
                    }
                }
            }
        }
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgIv: ImageView = itemView.findViewById(com.jayson.komm.common.R.id.img_iv)
    }
}
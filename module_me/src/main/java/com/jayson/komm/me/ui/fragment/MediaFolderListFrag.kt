package com.jayson.komm.me.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jayson.komm.common.base.BaseListFragment
import com.jayson.komm.common.bean.FileInfo
import com.jayson.komm.common.util.BitmapUtil
import com.jayson.komm.common.util.JumpUtils
import com.jayson.komm.common.util.LogUtils
import com.jayson.komm.common.util.MediaUtils
import com.jayson.komm.me.MeFragment
import com.jayson.komm.me.R
import com.jayson.komm.me.ui.page.MediaFileActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf

class MediaFolderListFrag : BaseListFragment<Pair<String, ArrayList<FileInfo>>>() {

    companion object {
        private const val TAG = "FolderListFrag"
    }

    enum class MediaType {
        IMAGE,
        VIDEO,
        AUDIO
    }

    private var fileList: MutableList<Pair<String, ArrayList<FileInfo>>> = mutableListOf()

    override fun setupInit() {
        setRefreshOrLoadEnable(
            refreshEnable = false,
            loadEnable = false,
            layoutManager = GridLayoutManager(context, 3)
        )
        when (tag) {
            MeFragment.TYPE_IMAGE -> {
                // 扫描图片文件
                scanMediaFile(MediaType.IMAGE)
            }
            MeFragment.TYPE_VIDEO -> {
                // 扫描视频文件
                scanMediaFile(MediaType.VIDEO)
            }
            MeFragment.TYPE_AUDIO -> {
                // 扫描音频文件
                scanMediaFile(MediaType.AUDIO)
            }
        }
    }

    /**
     * 创建一个方法来扫描手机所有媒体库资源
     */
    @SuppressLint("Range")
    private fun scanMediaFile(mediaType: MediaType) {
        lifecycleScope.launch(Dispatchers.IO) {
            val fileMap = when (mediaType) {
                MediaType.IMAGE -> MediaUtils.queryImages(context)
                MediaType.VIDEO -> MediaUtils.queryVideos(context)
                MediaType.AUDIO -> MediaUtils.queryAudios(context)
            }
            fileList = fileMap
                .toList()
                .sortedByDescending { (_, imageList) -> imageList.size }
                .toMutableList()
            LogUtils.d(TAG, "fileList: $fileList")
            withContext(Dispatchers.Main) {
                handleRefresh()
            }
        }
    }

    override fun getListData(): List<Pair<String, ArrayList<FileInfo>>> {
        // 这里是获取列表数据的逻辑
        return fileList
    }

    override fun noData(refreshOrLoad: Int) {
    }

    override fun createDataViewHolder(parent: ViewGroup): DataViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_media_folder, parent, false)
        return DataViewHolder(view)
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun bindDataViewHolder(
        holder: RecyclerView.ViewHolder,
        data: Pair<String, ArrayList<FileInfo>>?,
        position: Int
    ) {
        if (holder is DataViewHolder) {
            context?.let {
                if (data?.second?.size == 0) {
                    return
                }
                holder.folderTv.text = data?.first
                holder.numTv.text = data?.second?.size.toString()
                data?.second?.get(0)?.let { info ->
                    setFolderIv(holder.folderIv, info)
                    holder.folderIv.setOnClickListener {
                        val intent = Intent(activity, MediaFileActivity::class.java).apply {
                            putExtra(MeFragment.FILE_TYPE, tag)
                            // 将其序列化为字节数组,将字节数组添加到Intent中
                            val fileList = ProtoBuf.encodeToByteArray(data.second)
                            putExtra(MeFragment.FILE_LIST, fileList)
                        }
                        JumpUtils.startGoActivity(activity, intent)
                    }
                }
            }
        }
    }

    /**
     * 设置FolderIv
     */
    private fun setFolderIv(folderIv: ImageView, data: FileInfo) {
        val type = data.fileType ?: ""
        val path = data.path ?: ""
        if ((type.contains("image")) || (type.contains("video"))) {
            // 获取图片或视频缩略图
            Glide.with(this)
                .load(path)
                .into(folderIv)
        } else {
            // 获取其他文件的生成缩略图
            val text = type.split("/")[0]
            folderIv.setImageBitmap(BitmapUtil.makeBitmapWithText(text.uppercase()))
        }
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val folderIv: ImageView = itemView.findViewById(R.id.folder_iv)
        val folderTv: TextView = itemView.findViewById(R.id.folder_tv)
        val numTv: TextView = itemView.findViewById(R.id.num_tv)
    }
}
package com.jayson.komm.me.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
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
import com.jayson.komm.common.util.JumpUtils
import com.jayson.komm.common.util.LogUtils
import com.jayson.komm.me.MeFragment
import com.jayson.komm.me.ui.page.LocalFileActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.File

class FolderListFrag : BaseListFragment<Pair<String, ArrayList<FileInfo>>>() {

    companion object {
        private const val TAG = "FolderListFrag"
    }

    private var fileMap: MutableMap<String, ArrayList<FileInfo>> = mutableMapOf()
    private var fileList: MutableList<Pair<String, ArrayList<FileInfo>>> = mutableListOf()

    override fun setupInit() {
        setRefreshOrLoadEnable(
            refreshEnable = false,
            loadEnable = false,
            layoutManager = GridLayoutManager(context, 3)
        )
        when (tag) {
            MeFragment.TYPE_PICTURE -> {
                // 扫描图片文件
                scanAllPictures()
            }
            MeFragment.TYPE_VIDEO -> {
                // 扫描视频文件
            }
            MeFragment.TYPE_AUDIO -> {
                // 扫描音频文件
            }
        }
    }

    /**
     * 创建一个方法来扫描手机所有图片资源
     */
    @SuppressLint("Range")
    fun scanAllPictures() {
        lifecycleScope.launch(Dispatchers.IO) {
            val projection =
                arrayOf(
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.SIZE,
                    MediaStore.Images.Media.WIDTH,
                    MediaStore.Images.Media.HEIGHT,
                )
            val imagesUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val cursor: Cursor? =
                context?.contentResolver?.query(
                    imagesUri, projection, null, null, null
                )
            cursor?.use {
                val dataIndex: Int = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val bucketIndex: Int =
                    it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
                val nameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
                val widthColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)
                val heightColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)
                val dataColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                while (it.moveToNext()) {
                    val imagePath: String = it.getString(dataIndex)
                    val bucketName: String = it.getString(bucketIndex)
                    val fileName = it.getString(nameColumn)
                    val fileSize = it.getLong(sizeColumn)
                    val width = it.getInt(widthColumn)
                    val height = it.getInt(heightColumn)
                    val directory = File(it.getString(dataColumn)).parent ?: ""
                    val imageInfo = FileInfo(
                        fileName = fileName,
                        fileSize = fileSize,
                        width = width,
                        height = height,
                        directory = directory,
                        bucket = bucketName,
                        path = imagePath
                    )
                    if (fileMap.containsKey(bucketName)) {
                        fileMap[bucketName]?.add(imageInfo)
                    } else {
                        val imageList: ArrayList<FileInfo> = ArrayList()
                        imageList.add(imageInfo)
                        fileMap[bucketName] = imageList
                    }
                }
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
                .inflate(com.jayson.komm.common.R.layout.item_picture_folder, parent, false)
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
                    Glide.with(it)
                        .load(info.path)
                        .into(holder.folderIv)
                    holder.folderIv.setOnClickListener {
                        val intent = Intent(activity, LocalFileActivity::class.java).apply {
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

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val folderIv: ImageView = itemView.findViewById(com.jayson.komm.common.R.id.folder_iv)
        val folderTv: TextView = itemView.findViewById(com.jayson.komm.common.R.id.folder_tv)
        val numTv: TextView = itemView.findViewById(com.jayson.komm.common.R.id.num_tv)
    }
}
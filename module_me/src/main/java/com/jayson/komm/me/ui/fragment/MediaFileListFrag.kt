package com.jayson.komm.me.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jayson.komm.common.base.BaseListFragment
import com.jayson.komm.common.bean.FileInfo
import com.jayson.komm.common.util.BitmapUtil
import com.jayson.komm.common.util.JumpUtils
import com.jayson.komm.me.MeFragment
import com.jayson.komm.me.R

class MediaFileListFrag : BaseListFragment<FileInfo>() {

    companion object {
        private const val TAG = "FileListFrag"
    }

    private var fileList = arrayListOf<FileInfo>()

    private val isShowFile
        get() = (tag == MeFragment.TYPE_IMAGE) || (tag == MeFragment.TYPE_VIDEO)

    override fun setupInit() {
        val manager = if (isShowFile) {
            GridLayoutManager(context, 4)
        } else {
            LinearLayoutManager(context)
        }
        setRefreshOrLoadEnable(
            refreshEnable = false,
            loadEnable = false,
            layoutManager = manager
        )
    }

    /**
     * 刷新数据
     */
    fun refreshData(files: ArrayList<FileInfo>) {
        fileList = files
        handleRefresh()
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
                .inflate(R.layout.item_media_file, parent, false)
        return DataViewHolder(view)
    }

    override fun bindDataViewHolder(
        holder: RecyclerView.ViewHolder,
        data: FileInfo?,
        position: Int
    ) {
        if (holder is DataViewHolder) {
            val ctx = context ?: return
            data?.let { info ->
                if (isShowFile) {
                    holder.showIv.visibility = View.VISIBLE
                    Glide.with(ctx)
                        .load(info.path)
                        .placeholder(com.jayson.komm.res.R.mipmap.placeholder)
                        .into(holder.showIv)
                    holder.showIv.setOnClickListener {
                        JumpUtils.goShowMediaActivity(activity, position, fileList)
                    }
                } else {
                    holder.fileContainer.visibility = View.VISIBLE
                    val text = kotlin.runCatching { info.fileType.split("/")[0] }.getOrElse { "" }
                    holder.iconTv.setImageBitmap(BitmapUtil.makeBitmapWithText(text.uppercase()))
                    holder.nameTv.text = info.fileName
                    holder.pathTv.text = info.path
                    holder.fileContainer.setOnClickListener {
                        JumpUtils.goShowMediaActivity(activity, position, fileList)
                    }
                }
            }
        }
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val showIv: ImageView = itemView.findViewById(R.id.show_iv)
        val fileContainer: LinearLayout =
            itemView.findViewById(R.id.file_container)
        val iconTv: ImageView = itemView.findViewById(R.id.icon_iv)
        val nameTv: TextView = itemView.findViewById(R.id.name_tv)
        val pathTv: TextView = itemView.findViewById(R.id.path_tv)
    }
}
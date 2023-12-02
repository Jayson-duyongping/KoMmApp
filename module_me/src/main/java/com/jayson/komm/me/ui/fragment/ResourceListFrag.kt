package com.jayson.komm.me.ui.fragment

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jayson.komm.common.base.BaseListFragment
import com.jayson.komm.common.bean.FileInfo
import com.jayson.komm.common.util.BitmapUtil
import com.jayson.komm.common.util.JumpUtils
import com.jayson.komm.common.util.LogUtils
import com.jayson.komm.common.util.TitleBarUtils
import com.jayson.komm.data.bean.Classify
import com.jayson.komm.me.R
import com.jayson.komm.me.ui.page.ResourceActivity
import java.io.File

class ResourceListFrag : BaseListFragment<Classify>() {

    companion object {
        private const val TAG = "ResourceListFrag"
    }

    private var scanFolders = mutableListOf<Classify>()
    private var currentLoadedFiles = mutableListOf<Classify>()
    private var isNeedItemAnim = false

    override fun setupInit() {
        setRefreshOrLoadEnable(
            refreshEnable = false,
            loadEnable = true
        )
    }

    fun updateData(files: ArrayList<Classify>) {
        currentLoadedFiles.clear()
        scanFolders = files
        isNeedItemAnim = true
        handleRefresh()
    }

    fun setAllChecked(isChecked: Boolean) {
        currentLoadedFiles.forEach { it.isChecked = isChecked }
        setEditTitle()
    }

    fun getEditingFiles(): List<Classify> {
        return currentLoadedFiles.filter { it.isChecked }.toList()
    }

    fun setIsNeedItemAnim(isNeed: Boolean) {
        isNeedItemAnim = isNeed
    }

    override fun getListData(): List<Classify> {
        // 计算实际需要获取的元素数量，最多为20个
        val targetSize = minOf(scanFolders.size, 20)
        // 获取需要的元素子列表
        val targetList = scanFolders.subList(0, targetSize).toList()
        // 移除已获取的元素
        scanFolders.subList(0, targetSize).clear()
        // 将获取到的元素添加到已加载列表中
        currentLoadedFiles.addAll(targetList)
        return targetList
    }

    override fun noData(refreshOrLoad: Int) {
    }

    override fun createDataViewHolder(parent: ViewGroup): DataViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_resource_file, parent, false)
        return DataViewHolder(view)
    }

    override fun bindDataViewHolder(
        holder: RecyclerView.ViewHolder,
        data: Classify?,
        position: Int
    ) {
        if (holder is DataViewHolder) {
            LogUtils.d(TAG, "position:$position")
            val mActivity = activity as ResourceActivity
            val path = data?.path ?: return
            holder.fileTv.text = data.name
            setFileIv(holder.fileIv, data)
            val editPair = TitleBarUtils.isEditMode(mActivity)
            holder.itemView.apply {
                if (isNeedItemAnim) {
                    animation = AnimationUtils.loadAnimation(context, R.anim.animation_show)
                }
                setOnClickListener {
                    if (editPair.first) {
                        holder.fileCb.isChecked = !holder.fileCb.isChecked
                        return@setOnClickListener
                    }
                    val fileList = currentLoadedFiles.map { classify ->
                        // 在这里编写将源对象转换为目标对象的转换逻辑 - 返回目标对象
                        FileInfo(
                            fileName = classify.name ?: "",
                            fileType = classify.fileType ?: "",
                            path = classify.path ?: ""
                        )
                    }
                    JumpUtils.goShowMediaActivity(activity, position, fileList)
                }
                setOnLongClickListener {
                    if (editPair.first) {
                        return@setOnLongClickListener false
                    }
                    LogUtils.d(TAG, "setOnLongClickListener")
                    mActivity.showListEditMode()
                    return@setOnLongClickListener true
                }
            }
            holder.fileCb.apply {
                visibility = if (editPair.first) View.VISIBLE else View.GONE
                isChecked = data.isChecked
                setOnCheckedChangeListener { _, isChecked ->
                    currentLoadedFiles[position].isChecked = isChecked
                    setEditTitle()
                }
            }
        }
    }

    /**
     * 设置编辑标题栏文字
     */
    private fun setEditTitle() {
        val activity = activity ?: return
        val countChecked = currentLoadedFiles.count { it.isChecked }
        val editTitle = if (countChecked == 0) {
            "请选择项目"
        } else {
            "已选择${countChecked}项"
        }
        LogUtils.d(TAG, "已选择${countChecked}项")
        TitleBarUtils.setTitleText(activity, editTitle)
    }

    /**
     * 设置FileIv
     */
    private fun setFileIv(fileIv: ImageView, data: Classify) {
        val type = data.fileType ?: ""
        val path = data.path ?: ""
        if ((type.contains("image")) || (type.contains("video"))) {
            // 获取图片或视频缩略图
            Glide.with(this)
                .load(Uri.fromFile(File(path)))
                .override(100, 100)
                .into(fileIv)
        } else {
            // 获取其他文件的生成缩略图
            val text = type.split("/")[0]
            fileIv.setImageBitmap(BitmapUtil.makeBitmapWithText(text.uppercase()))
        }
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fileIv: ImageView = itemView.findViewById(R.id.file_iv)
        val fileTv: TextView = itemView.findViewById(R.id.file_tv)
        val fileCb: CheckBox = itemView.findViewById(R.id.file_cb)
    }
}
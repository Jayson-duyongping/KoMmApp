package com.jayson.komm.me.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.jayson.komm.common.base.BaseListFragment
import com.jayson.komm.common.util.LogUtils
import com.jayson.komm.common.util.ResourceUtil
import com.jayson.komm.data.bean.Classify
import com.jayson.komm.data.manager.FileDataManager
import com.jayson.komm.me.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

class ResourceGroupFrag(private val scanFileDir: String?) : BaseListFragment<Classify>() {

    companion object {
        private const val TAG = "ResourceGroupFrag"
    }

    private var cardColors = listOf<Int>()

    private var scanFolders = mutableListOf<Classify>()
    private var pathStack = Stack<String>()

    override fun setupInit() {
        setRefreshOrLoadEnable(
            refreshEnable = false,
            loadEnable = false
        )
        context?.let {
            cardColors = ResourceUtil.getCommonColors(it)
        }
        scanFileDir?.let {
            updateData(it)
        }
    }

    private fun updateData(path: String, isAdd: Boolean = true) {
        lifecycleScope.launch(Dispatchers.IO) {
            scanFolders =
                FileDataManager.scanCurrentFiles(File(path)).first
            handleRefresh()
            if (isAdd) {
                pathStack.add(path)
            }
            withContext(Dispatchers.Main) {
                onDataPassListener?.onNoticePath(path)
            }
        }
    }

    /**
     * 返回上级目录
     */
    fun backParentPath() {
        kotlin.runCatching {
            pathStack.pop()
            updateData(pathStack.peek(), false)
        }.onFailure {
            LogUtils.e(TAG, "backParentPath, e:$it")
        }
    }

    /**
     * 获取当前路径
     */
    fun getCurrentPath(): String = pathStack.peek()

    override fun getListData(): List<Classify> {
        return scanFolders
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
            val path = data?.path ?: return
            holder.fileTv.text = data.name
            holder.fileCv.apply {
                setCardBackgroundColor(cardColors[position % cardColors.size])
            }
            holder.itemView.apply {
                setOnClickListener {
                    updateData(path)
                }
            }
        }
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fileCv: CardView = itemView.findViewById(R.id.file_cv)
        val fileTv: TextView = itemView.findViewById(R.id.file_tv)
    }

    /**
     * fragment通信
     */
    var onDataPassListener: OnDataPassListener? = null

    interface OnDataPassListener {
        fun onNoticePath(path: String)
    }

    override fun onDestroy() {
        super.onDestroy()
        onDataPassListener = null
    }
}
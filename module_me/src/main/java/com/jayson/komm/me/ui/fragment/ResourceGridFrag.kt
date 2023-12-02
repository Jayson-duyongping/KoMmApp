package com.jayson.komm.me.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jayson.komm.common.base.BaseListFragment
import com.jayson.komm.common.util.ResourceUtil
import com.jayson.komm.common.util.TitleBarUtils
import com.jayson.komm.data.bean.Classify
import com.jayson.komm.me.R
import com.jayson.komm.me.ui.page.ResourceActivity

class ResourceGridFrag : BaseListFragment<Classify>() {

    companion object {
        private const val TAG = "ResourceGridFrag"
    }

    private var cardColors = listOf<Int>()

    private var scanFolders = mutableListOf<Classify>()

    override fun setupInit() {
        setRefreshOrLoadEnable(
            refreshEnable = false,
            loadEnable = false,
            layoutManager = GridLayoutManager(context, 3)
        )
        context?.let {
            cardColors = ResourceUtil.getCommonColors(it)
        }
    }

    fun updateData(files: ArrayList<Classify>) {
        scanFolders = files
        handleRefresh()
    }

    override fun getListData(): List<Classify> {
        return scanFolders
    }

    override fun noData(refreshOrLoad: Int) {
    }

    override fun createDataViewHolder(parent: ViewGroup): DataViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_resource_folder, parent, false)
        return DataViewHolder(view)
    }

    override fun bindDataViewHolder(
        holder: RecyclerView.ViewHolder,
        data: Classify?,
        position: Int
    ) {
        if (holder is DataViewHolder) {
            val mActivity = (activity as ResourceActivity)
            holder.itemView.animation = AnimationUtils.loadAnimation(context, R.anim.animation_show)
            holder.folderTv.text = data?.name
            holder.folderCard.apply {
                setCardBackgroundColor(cardColors[position % cardColors.size])
                setOnClickListener {
                    if (TitleBarUtils.isEditMode(mActivity).first) {
                        return@setOnClickListener
                    }
                    mActivity.updateData(data?.path)
                }
                setOnLongClickListener {
                    mActivity.showGridEditMode(data)
                    return@setOnLongClickListener true
                }
            }
        }
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val folderCard: CardView = itemView.findViewById(R.id.folder_card)
        val folderTv: TextView = itemView.findViewById(R.id.folder_tv)
    }
}
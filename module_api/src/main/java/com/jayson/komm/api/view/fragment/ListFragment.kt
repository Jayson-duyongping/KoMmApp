package com.jayson.komm.api.view.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jayson.komm.api.R
import com.jayson.komm.api.bean.Mm
import com.jayson.komm.api.net.MmHttpService
import com.jayson.komm.common.base.BaseListFragment

class ListFragment : BaseListFragment<Mm>() {

    override fun setupInit() {
        setRefreshOrLoadEnable(refreshEnable = true, loadEnable = true)
    }

    override fun getListData(): List<Mm>? {
        // 这里是获取列表数据的逻辑
        return MmHttpService.getMmList()
    }

    override fun noData(refreshOrLoad: Int) {
        // 没有数据了
        val message = if (refreshOrLoad == 0) "刷新无数据" else "加载无数据"
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun createDataViewHolder(parent: ViewGroup): DataViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_mm, parent, false)
        return DataViewHolder(view)
    }

    override fun bindDataViewHolder(holder: RecyclerView.ViewHolder, data: Mm?, position: Int) {
        if (holder is DataViewHolder) {
            context?.let {
                Glide.with(it)
                    .load(data?.imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(com.jayson.komm.res.R.mipmap.placeholder)
                    .into(holder.mmIv)
            }
        }
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tipTv: TextView = itemView.findViewById(R.id.tip_tv)
        val mmIv: ImageView = itemView.findViewById(R.id.mm_iv)
    }
}
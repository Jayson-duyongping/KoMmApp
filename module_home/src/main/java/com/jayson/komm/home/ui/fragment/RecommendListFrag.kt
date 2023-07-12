package com.jayson.komm.home.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jayson.komm.common.base.BaseListFragment
import com.jayson.komm.common.bean.Card
import com.jayson.komm.home.R

class RecommendListFrag : BaseListFragment<Card>() {

    override fun setupInit() {
        setRefreshOrLoadEnable(refreshEnable = false, loadEnable = false)
    }

    override fun getListData(): List<Card>? {
        // 这里是获取列表数据的逻辑
        return listOf(
            Card(title = "美女推荐", content = "原画CG，绘制大美女", imageResource = R.mipmap.recommend_list_1),
            Card(title = "游戏推荐", content = "赛博朋克2077新DLC-昔日街道", imageResource = R.mipmap.recommend_list_2),
            Card(title = "动漫推荐", content = "新刀剑神域-亚丝娜", imageResource = R.mipmap.recommend_list_3)
        )
    }

    override fun noData(refreshOrLoad: Int) {
        // 没有数据了
        val message = if (refreshOrLoad == 0) "刷新无数据" else "加载无数据"
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun createDataViewHolder(parent: ViewGroup): DataViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(com.jayson.komm.common.R.layout.item_card_1, parent, false)
        return DataViewHolder(view)
    }

    override fun bindDataViewHolder(holder: RecyclerView.ViewHolder, data: Card?, position: Int) {
        if (holder is DataViewHolder) {
            context?.let {
                holder.titleTv.text = data?.title
                holder.contentTv.text = data?.content
                Glide.with(it)
                    .load(data?.imageResource)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(com.jayson.komm.res.R.mipmap.placeholder)
                    .into(holder.cardIv)
            }
        }
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTv: TextView = itemView.findViewById(com.jayson.komm.common.R.id.card_title)
        val contentTv: TextView = itemView.findViewById(com.jayson.komm.common.R.id.card_content)
        val cardIv: ImageView = itemView.findViewById(com.jayson.komm.common.R.id.card_iv)
    }
}
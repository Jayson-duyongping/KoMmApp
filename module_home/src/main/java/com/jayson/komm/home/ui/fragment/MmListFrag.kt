package com.jayson.komm.home.ui.fragment

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.bumptech.glide.request.RequestOptions
import com.jayson.komm.common.base.BaseListFragment
import com.jayson.komm.common.bean.Card
import com.jayson.komm.common.util.ScreenUtil
import com.jayson.komm.common.view.custom.RoundImageView
import com.jayson.komm.home.R
import java.security.MessageDigest


class MmListFrag : BaseListFragment<Card>() {

    companion object {
        private const val TAG = "MmListFrag"
    }

    override fun setupInit() {
        setRefreshOrLoadEnable(refreshEnable = false, loadEnable = false)
    }

    override fun getListData(): List<Card> {
        // 这里是获取列表数据的逻辑
        return listOf(
            Card(
                title = "清纯",
                images = listOf(
                    R.mipmap.mm_purity_1,
                    R.mipmap.mm_purity_2,
                    R.mipmap.mm_purity_3,
                    R.mipmap.mm_purity_4
                )
            ),
            Card(
                title = "性感",
                images = listOf(
                    R.mipmap.mm_sexy_1,
                    R.mipmap.mm_sexy_2,
                    R.mipmap.mm_sexy_3,
                    R.mipmap.mm_sexy_4
                )
            ),
            Card(
                title = "另类",
                images = listOf(
                    R.mipmap.mm_alien_1,
                    R.mipmap.mm_alien_2,
                    R.mipmap.mm_alien_3,
                    R.mipmap.mm_alien_4
                )
            )
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
                .inflate(com.jayson.komm.common.R.layout.item_card_2, parent, false)
        return DataViewHolder(view)
    }

    override fun bindDataViewHolder(holder: RecyclerView.ViewHolder, data: Card?, position: Int) {
        if (holder is DataViewHolder) {
            context?.let {
                holder.titleTv.text = data?.title
                data?.images?.let { images ->
                    for ((i, img) in images.withIndex()) {
                        val imageView = RoundImageView(it).apply {
                            layoutParams = GridLayout.LayoutParams().apply {
                                width = 0
                                height = ScreenUtil.dp2px(context, 150f)
                                columnSpec = GridLayout.spec(i % 2, 1f)
                                rowSpec = GridLayout.spec(i / 2, 1f)
                                val margin = ScreenUtil.dp2px(context, 4f)
                                setMargins(margin, margin, margin, margin)
                            }
                            setCornerRadius(ScreenUtil.dp2px(it,10f).toFloat())
                            scaleType = ImageView.ScaleType.CENTER_CROP
                            // 设置图片
                            setImageResource(img)
                            Glide.with(this)
                                .load(img)
                                .apply(
                                    RequestOptions.bitmapTransform(
                                        RoundedCornerCenterCrop(
                                            ScreenUtil.dp2px(context, 16f)
                                        )
                                    )
                                )
                                .into(this);
                        }
                        holder.cardGrid.addView(imageView)
                    }
                }
            }
        }
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTv: TextView = itemView.findViewById(com.jayson.komm.common.R.id.card_title)
        val cardGrid: GridLayout = itemView.findViewById(com.jayson.komm.common.R.id.card_grid)
    }

    /**
     * 切圆角
     */
    inner class RoundedCornerCenterCrop(private val radius: Int = 0) : BitmapTransformation() {
        override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        }

        override fun transform(
            pool: BitmapPool,
            toTransform: Bitmap,
            outWidth: Int,
            outHeight: Int
        ): Bitmap {
            val bitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight)
            return TransformationUtils.roundedCorners(pool, bitmap, radius)
        }
    }
}
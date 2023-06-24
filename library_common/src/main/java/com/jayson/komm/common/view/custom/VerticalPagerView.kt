package com.jayson.komm.common.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.jayson.komm.common.R
import com.jayson.komm.common.util.ResourceUtil
import com.jayson.komm.common.util.ScreenUtil.dp2px

class VerticalPagerView<T> @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val viewPager: ViewPager2
    private val indicator: LinearLayout

    private var dataList: List<T> = listOf()
    private var cusAdapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>? = null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_vertical_pager, this, true)
        viewPager = view.findViewById(R.id.viewPager2)
        indicator = view.findViewById(R.id.indicator_ll)
    }

    fun initVerticalPager(
        dataList: List<T>,
        adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null
    ) {
        this.dataList = dataList
        this.cusAdapter = adapter ?: MyAdapter()
        initViewPager()
        initIndicator()
    }


    private fun initViewPager() {
        viewPager.apply {
            adapter = cusAdapter
            orientation = ViewPager2.ORIENTATION_VERTICAL
            offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
            // 滑动监听
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updateIndicator(position)
                }
            })
        }
    }

    private fun initIndicator() {
        for (i in 0 until (cusAdapter?.itemCount ?: 1)) {
            val view = View(context)
            view.setBackgroundResource(R.drawable.selector_indicator)
            val params = LayoutParams(
                dp2px(context, 6f),
                dp2px(context, 6f)
            )
            if (i > 0) {
                params.topMargin = dp2px(context, 6f)
            }
            view.layoutParams = params
            indicator.addView(view)
        }
    }

    private fun updateIndicator(position: Int) {
        for (i in 0 until indicator.childCount) {
            val view = indicator.getChildAt(i)
            view.isSelected = i == position
        }
    }

    /**
     * 内部Adapter，如果外部不传自定义的adapter或数据类，默认用此adapter
     */
    inner class MyAdapter : RecyclerView.Adapter<MyAdapter.MyHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false)
            return MyHolder(view)
        }

        override fun onBindViewHolder(holder: MyHolder, position: Int) {
            val data = dataList[position]
            holder.bind(data)
        }

        override fun getItemCount(): Int {
            return dataList.size
        }

        inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val containerFl: FrameLayout = itemView.findViewById(R.id.container_fl)
            private val titleTv: TextView = itemView.findViewById(R.id.title_tv)
            fun bind(item: T) {
                if (item is Fragment) {
                    val fragmentManager =
                        (itemView.context as AppCompatActivity).supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.replace(itemView.id, item)
                    transaction.commit()
                    return
                }
                if (item is View) {
                    containerFl.addView(item)
                    return
                }
                if (item is String) {
                    titleTv.visibility = View.VISIBLE
                    titleTv.text = item
                }
                context?.let {
                    val colors = ResourceUtil.getCommonColors(it)
                    itemView.setBackgroundColor(colors[position % colors.size])
                }
            }
        }
    }
}
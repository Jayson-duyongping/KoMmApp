package com.jayson.komm.common.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.jayson.komm.common.R
import com.jayson.komm.common.bean.Banner
import com.jayson.komm.common.util.ScreenUtil.dp2px
import kotlin.math.abs

/**
 * 自定义轮播Banner, 解决滑动冲突
 */
class BannerView<T> @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val viewPager: ViewPager2
    private val indicator: LinearLayout

    private var banners: List<T> = listOf()
    private var cusAdapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>? = null
    private val handler = Handler()

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_banner, this, true)
        viewPager = findViewById(R.id.viewPager)
        indicator = findViewById(R.id.indicator_ll)
    }

    fun initBanner(
        bannerList: List<T>,
        adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null
    ) {
        this.banners = bannerList
        this.cusAdapter = adapter ?: BannerAdapter()
        initViewPager()
        initIndicator()
    }

    fun startAutoPlay() {
        handler.postDelayed(autoPlayRunnable, 3000)
    }

    fun stopAutoPlay() {
        handler.removeCallbacks(autoPlayRunnable)
    }

    private val autoPlayRunnable = object : Runnable {
        override fun run() {
            viewPager.apply {
                if (currentItem == banners.size - 1) {
                    currentItem = 0
                } else {
                    currentItem += 1
                }
            }
            handler.postDelayed(this, 3000)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViewPager() {
        viewPager.apply {
            adapter = cusAdapter
            offscreenPageLimit = banners.size
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            // 滑动监听
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updateIndicator(position % banners.size)
                    cusAdapter?.let {
                        if (position == it.itemCount - 1) {
                            setCurrentItem(0, true)
                        }
                    }
                }
            })
            // 设置动画
            setPageTransformer { page, position ->
                val percent = 1 - abs(position)
                val margin = dp2px(context, 16f)
                page.scaleY = 0.85f + percent * 0.15f
                page.translationY = margin * abs(position)
            }
            // 监听 ViewPager2 的触摸事件
            setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        stopAutoPlay()
                    }
                    MotionEvent.ACTION_UP -> {
                        startAutoPlay()
                    }
                }
                false
            }
        }
    }

    private fun initIndicator() {
        for (i in banners.indices) {
            val view = View(context)
            view.setBackgroundResource(R.drawable.selector_indicator)
            val params = LinearLayout.LayoutParams(
                dp2px(context, 6f),
                dp2px(context, 6f)
            )
            if (i > 0) {
                params.leftMargin = dp2px(context, 6f)
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
     * 内部Adapter，如果外部不传自定义的adapter或数据类，默认用此adapter和Banner数据类
     */
    inner class BannerAdapter : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): BannerAdapter.BannerViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_banner, parent, false)
            return BannerViewHolder(view)
        }

        override fun onBindViewHolder(holder: BannerAdapter.BannerViewHolder, position: Int) {
            val banner = banners[position % banners.size]
            holder.bind(banner)
        }

        override fun getItemCount(): Int {
            return Int.MAX_VALUE
        }

        inner class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val imageView = itemView.findViewById<ImageView>(R.id.banner_iv)
            fun bind(item: T) {
                if (item is Banner) {
                    imageView.setImageResource(item.imageResource)
                }
            }
        }
    }

    /****************************  滑动冲突解决逻辑 Start  ****************************/
    private var disallowParentInterceptDownEvent = true
    private var startX = 0
    private var startY = 0

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val doNotNeedIntercept = (!viewPager.isUserInputEnabled
                || (viewPager.adapter != null
                && viewPager.adapter!!.itemCount <= 1))
        if (doNotNeedIntercept) {
            return super.onInterceptTouchEvent(ev)
        }
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev.x.toInt()
                startY = ev.y.toInt()
                parent.requestDisallowInterceptTouchEvent(!disallowParentInterceptDownEvent)
            }
            MotionEvent.ACTION_MOVE -> {
                val endX = ev.x.toInt()
                val endY = ev.y.toInt()
                val disX = abs(endX - startX)
                val disY = abs(endY - startY)
                if (viewPager.orientation == ViewPager2.ORIENTATION_VERTICAL) {
                    onVerticalActionMove(endY, disX, disY)
                } else if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                    onHorizontalActionMove(endX, disX, disY)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> parent.requestDisallowInterceptTouchEvent(
                false
            )
        }
        return super.onInterceptTouchEvent(ev)
    }

    private fun onHorizontalActionMove(endX: Int, disX: Int, disY: Int) {
        if (viewPager.adapter == null) {
            return
        }
        if (disX > disY) {
            val currentItem = viewPager.currentItem
            val itemCount = viewPager.adapter!!.itemCount
            if (currentItem == 0 && endX - startX > 0) {
                parent.requestDisallowInterceptTouchEvent(false)
            } else {
                parent.requestDisallowInterceptTouchEvent(
                    currentItem != itemCount - 1
                            || endX - startX >= 0
                )
            }
        } else if (disY > disX) {
            parent.requestDisallowInterceptTouchEvent(false)
        }
    }

    private fun onVerticalActionMove(endY: Int, disX: Int, disY: Int) {
        if (viewPager.adapter == null) {
            return
        }
        val currentItem = viewPager.currentItem
        val itemCount = viewPager.adapter!!.itemCount
        if (disY > disX) {
            if (currentItem == 0 && endY - startY > 0) {
                parent.requestDisallowInterceptTouchEvent(false)
            } else {
                parent.requestDisallowInterceptTouchEvent(
                    currentItem != itemCount - 1
                            || endY - startY >= 0
                )
            }
        } else if (disX > disY) {
            parent.requestDisallowInterceptTouchEvent(false)
        }
    }
    /****************************  滑动冲突解决逻辑 End  ****************************/
}
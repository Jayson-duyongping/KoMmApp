package com.jayson.komm.common.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jayson.komm.common.bean.Card
import com.jayson.komm.common.ext.addClickScale
import com.jayson.komm.common.util.ResourceUtil

/**
 * 横向Card列表
 */
class HorizontalCardListView : LinearLayout {

    private lateinit var recyclerView: RecyclerView

    private var cards: List<Card> = listOf()

    // 是否配置卡片颜色
    private var cardColors: List<Int>? = ResourceUtil.getCommonColors(context)

    // 是否设置卡片图片背景
    private var isImageBackGround: Boolean = false

    // 是否显示文字
    private var isShowText: Boolean = false

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        recyclerView = RecyclerView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            // 解决与父级滑动冲突：在RecyclerView上设置addOnItemTouchListener，当用户按下时，禁用父 ViewGroup 上的触摸事件。当用户松开时，启用它们。
            addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    when (e.actionMasked) {
                        MotionEvent.ACTION_DOWN -> parent.requestDisallowInterceptTouchEvent(true)
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> parent.requestDisallowInterceptTouchEvent(
                            false
                        )
                    }
                    return false
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })
        }
        addView(recyclerView)
    }

    fun initCardView(
        cards: List<Card>,
        adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null
    ) {
        this.cards = cards
        recyclerView.adapter = adapter ?: CardAdapter()
    }

    fun configCard(
        cardColors: List<Int> = ResourceUtil.getCommonColors(context),
        isImageBackGround: Boolean = false,
        isShowText: Boolean = false
    ) {
        this.cardColors = cardColors
        this.isImageBackGround = isImageBackGround
        this.isShowText = isShowText
    }


    /**
     * 内部Adapter，如果外部不传自定义的adapter或数据类，默认用此adapte和Card数据类
     */
    inner class CardAdapter : RecyclerView.Adapter<CardAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(com.jayson.komm.common.R.layout.item_card, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val card = cards[position]
            holder.card.apply {
                cardColors?.let {
                    setCardBackgroundColor(it[position % it.size])
                }
            }.addClickScale {}
            if (isImageBackGround) {
                holder.imageIv.setBackgroundResource(card.imageResource)
            }
            if (isShowText) {
                holder.titleTv.visibility = View.VISIBLE
                holder.titleTv.text = card.title
            }
        }

        override fun getItemCount() = cards.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val card: CardView = itemView.findViewById(com.jayson.komm.common.R.id.card_view)
            val imageIv: ImageView = itemView.findViewById(com.jayson.komm.common.R.id.card_iv)
            val titleTv: TextView = itemView.findViewById(com.jayson.komm.common.R.id.card_title)
        }
    }
}
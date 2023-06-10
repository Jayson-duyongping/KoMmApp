package com.jayson.komm.api.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @Author: Jayson
 * @CreateDate: 2023/4/2 22:12
 * @Version: 1.0
 * @Description:使用Kotlin自定义的一个瀑布流RecyclerView，每列显示图片数目不一定相等，图片高度不一定相等，但是每列的高度尽量平衡。
 */
class WaterFlowRecyclerView(context: Context, attrs: AttributeSet?) :
    RecyclerView(context, attrs) {
    private var columnCount = 2 // 默认列数为2
    private var columnHeights = IntArray(columnCount) { 0 } // 列高度数组
    private var columnWidth = 0 // 列宽度

    init {
        val layoutManager = CustomLayoutManager()
        setLayoutManager(layoutManager)
    }

    /**
     * 设定列数
     */
    fun setColumnCount(columnCount: Int) {
        this.columnCount = columnCount
        columnHeights = IntArray(columnCount) { 0 }
        // 列数变化需要重新布局
        requestLayout()
    }

    fun getColumnCount(): Int {
        return columnCount
    }

    private inner class CustomLayoutManager : LayoutManager() {
        override fun generateDefaultLayoutParams(): LayoutParams {
            return LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        override fun onLayoutChildren(
            recycler: RecyclerView.Recycler?,
            state: State?
        ) {
            if (itemCount == 0 || state?.isPreLayout == true) return
            detachAndScrapAttachedViews(recycler!!)
            columnWidth = width / columnCount
            for (i in columnHeights.indices) {
                columnHeights[i] = 0
            }
            for (i in 0 until itemCount) {
                val view = recycler.getViewForPosition(i)
                addView(view)
                measureChildWithMargins(view, 0, 0)
                val width = getDecoratedMeasuredWidth(view)
                val height = getDecoratedMeasuredHeight(view)
                val columnIndex = findColumnIndex()
                layoutDecoratedWithMargins(
                    view,
                    columnIndex * columnWidth,
                    columnHeights[columnIndex],
                    columnIndex * columnWidth + width,
                    columnHeights[columnIndex] + height
                )
                columnHeights[columnIndex] += height
            }
        }

        private fun findColumnIndex(): Int {
            var minHeight = columnHeights[0]
            var columnIndex = 0
            for (i in columnHeights.indices) {
                if (columnHeights[i] < minHeight) {
                    minHeight = columnHeights[i]
                    columnIndex = i
                }
            }
            return columnIndex
        }
    }
}
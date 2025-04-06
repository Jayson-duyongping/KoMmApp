package com.jayson.komm.dev.view

import android.annotation.SuppressLint
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.dev.R
import com.jayson.komm.dev.databinding.ActivityConstraintBinding

/**
 * 注意事项
 * 1.ViewStub.inflate之后，ViewStub将不存在，其属性会覆盖子view,所以以ViewStub的id设置约束都是不可行的
 * 2.setOnInflateListener中重新设置约束，主要是子view以及上下两个view的关系
 * 3.ViewStub的layout_height会覆盖子view的根布局，在子view中，可以用一个根布局嵌套，在里面设置具体的高度
 */
class ConstraintActivity : BaseActivity() {

    companion object {
        private const val TAG = "ConstraintActivity"

    }
    private lateinit var binding: ActivityConstraintBinding

    private lateinit var tempView: LinearLayout

    @SuppressLint("RestrictedApi")
    override fun initView() {
        super.initView()
        // 初始化binding
        binding = ActivityConstraintBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 设置 ViewStub 的加载监听（可选）
        binding.stubView.setOnInflateListener { _, view ->
            // 设置 headerView 的底部约束为展开视图的顶部
            setConstraints(binding.headerView, bottomToTop = view.id)
            // 设置展开视图的顶部和底部约束
            setConstraints(view, topToBottom = binding.headerView.id, bottomToTop = binding.contentView.id)
            // 设置 contentView 的顶部约束为展开视图的底部
            setConstraints(binding.contentView, topToBottom = view.id)
        }
        binding.stubView.inflate()

        // 处理tempView
        tempView = findViewById(R.id.temp_view)
        tempView.postDelayed({
            tempView.visibility = View.GONE
        },2000)

    }

    // 封装函数来设置约束布局参数
    private fun setConstraints(
        view: View,
        topToBottom: Int? = null,
        bottomToTop: Int? = null,
        height: Int? = null
    ) {
        val params = view.layoutParams as ConstraintLayout.LayoutParams
        topToBottom?.let { params.topToBottom = it }
        bottomToTop?.let { params.bottomToTop = it }
        height?.let { params.height = it }
        view.layoutParams = params
    }
}
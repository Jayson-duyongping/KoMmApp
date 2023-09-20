package com.jayson.komm.common.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jayson.komm.common.R
import com.jayson.komm.common.util.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 一个父类BaseListFragment
 * --可刷新和加载更多的列表Fragment
 */
abstract class BaseListFragment<T : Any> : Fragment() {

    companion object {
        private const val TAG = "BaseListFragment"
    }

    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var recyclerView: RecyclerView? = null
    private var moreLayout: TextView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    private var dataList: MutableList<T> = mutableListOf()
    private var isRefreshing = false
    private var isLoading = false

    // 是否可刷新
    private var isEnableRefresh = true

    // 是否可加载
    private var isEnableLoading = true

    // 底部加载更多类型
    private var currentLoadMoreState = LoadMoreType.HIDE

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupInit()
        val view = if (isEnableRefresh) {
            inflater.inflate(R.layout.fragment_base_refresh_list, container, false)
        } else {
            inflater.inflate(R.layout.fragment_base_list, container, false)
        }
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        recyclerView = view.findViewById(R.id.recycler_view)
        moreLayout = view.findViewById(R.id.more_layout)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout?.setOnRefreshListener {
            loadData()
        }
        adapter = CusListAdapter()
        recyclerView?.let {
            it.itemAnimator = null
            it.layoutManager = layoutManager ?: LinearLayoutManager(context)
            it.adapter = adapter
            // 解决滑动卡顿
            it.isNestedScrollingEnabled = false
            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    // 滑动到底部，触发“加载更多”操作
                    if (isEnableLoading && (dy > 0) && (!recyclerView.canScrollVertically(1))) {
                        if (currentLoadMoreState == LoadMoreType.NO_DATA) {
                            return
                        }
                        updateLoadMareLayout(LoadMoreType.LOAD_MORE)
                        loadMoreData()
                    }
                }
            })
        }
        loadData()
    }

    /**
     * onViewCreated中初始化操作
     */
    abstract fun setupInit()

    /**
     * 子类实现数据处理
     */
    abstract fun getListData(): List<T>?
    abstract fun noData(refreshOrLoad: Int)

    /**
     * 子类实现布局与绑定holder
     */
    abstract fun createDataViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    abstract fun bindDataViewHolder(holder: RecyclerView.ViewHolder, data: T?, position: Int)

    enum class LayoutManagerType {
        Linear,
        Grid,
        StaggeredGrid
    }

    enum class LoadMoreType {
        LOAD_MORE,
        NO_DATA,
        HIDE
    }

    /**
     * 设置不可刷新或加载更多，一般在setupInit中实现
     */
    open fun setRefreshOrLoadEnable(
        refreshEnable: Boolean = true,
        loadEnable: Boolean = true,
        managerType: LayoutManagerType? = null,
        layoutManager: RecyclerView.LayoutManager? = null
    ) {
        // 设置是否可刷新或加载
        isEnableRefresh = refreshEnable
        isEnableLoading = loadEnable
        if (!loadEnable) {
            updateLoadMareLayout(LoadMoreType.HIDE)
        }
        // 设置LayoutManager样式
        this.layoutManager = layoutManager ?: when (managerType) {
            LayoutManagerType.Grid -> {
                // 网格布局管理器
                GridLayoutManager(context, 2)
            }
            LayoutManagerType.StaggeredGrid -> {
                // 瀑布流布局管理器
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
                    gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
                }
            }
            else -> {
                // 线性布局管理器
                LinearLayoutManager(context).apply {
                    orientation = LinearLayoutManager.VERTICAL;
                }
            }
        }
    }

    /**
     * 提供给外部刷新数据
     */
    open fun handleRefresh() {
        LogUtils.d(TAG, "handleRefresh")
        loadData()
    }

    open fun handleLoadMore(loadMoreBeforeListener: () -> Unit) {
        LogUtils.d(TAG, "handleLoadMore")
        if (currentLoadMoreState == LoadMoreType.NO_DATA) {
            return
        }
        updateLoadMareLayout(LoadMoreType.LOAD_MORE)
        loadMoreBeforeListener.invoke()
        recyclerView?.postDelayed({
            loadMoreData()
        }, 200)
    }

    open fun notifyItemRangeChanged() {
        LogUtils.d(TAG, "notifyItemRangeChanged")
        adapter?.notifyItemRangeChanged(0, dataList.size)
    }

    protected fun getRecyclerView(): RecyclerView? = recyclerView

    @SuppressLint("NotifyDataSetChanged")
    private fun loadData() {
        LogUtils.d(TAG, "loadData")
        isRefreshing = true
        updateLoadMareLayout(LoadMoreType.HIDE)
        lifecycleScope.launch(Dispatchers.IO) {
            getListData()?.let {
                dataList.clear()
                dataList.addAll(it)
                withContext(Dispatchers.Main) {
                    if (it.isEmpty()) {
                        noData(0)
                    }
                    adapter?.notifyDataSetChanged()
                    swipeRefreshLayout?.isRefreshing = false
                    isRefreshing = false
                }
            } ?: let {
                swipeRefreshLayout?.isRefreshing = false
                isRefreshing = false
            }
        }
    }

    private fun loadMoreData() {
        LogUtils.d(TAG, "loadMoreData")
        isLoading = true
        lifecycleScope.launch(Dispatchers.IO) {
            getListData()?.let {
                val startPosition = dataList.size
                dataList.addAll(it)
                withContext(Dispatchers.Main) {
                    if (it.isEmpty()) {
                        updateLoadMareLayout(LoadMoreType.NO_DATA)
                        noData(1)
                        return@withContext
                    }
                    adapter?.notifyItemRangeInserted(startPosition, dataList.size)
                    isLoading = false
                    updateLoadMareLayout(LoadMoreType.HIDE)
                }
            }
        }
    }

    /**
     * 一个MoreListAdapter
     * --包含加载更多逻辑
     */
    inner class CusListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        /**
         * 创建不同的holder
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            // 子类实现特性
            return createDataViewHolder(parent)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val data = dataList[position]
            // 子类实现特性
            bindDataViewHolder(holder, data, position)
        }

        override fun getItemCount(): Int {
            return dataList.size
        }
    }

    /**
     * 更新加载更多Layout
     */
    private fun updateLoadMareLayout(loadMoreType: LoadMoreType) {
        LogUtils.d(TAG, "updateLoadMareLayout, loadMoreType:$loadMoreType")
        currentLoadMoreState = loadMoreType
        when (loadMoreType) {
            LoadMoreType.LOAD_MORE -> {
                moreLayout?.apply {
                    visibility = View.VISIBLE
                    text = "加载更多"
                    recyclerView?.scrollToPosition((adapter?.itemCount ?: 1) - 1)
                }
            }
            LoadMoreType.NO_DATA -> {
                moreLayout?.apply {
                    visibility = View.VISIBLE
                    text = "到底了"
                    recyclerView?.scrollToPosition((adapter?.itemCount ?: 1) - 1)
                }
            }
            LoadMoreType.HIDE -> {
                moreLayout?.visibility = View.GONE
            }
        }
    }
}
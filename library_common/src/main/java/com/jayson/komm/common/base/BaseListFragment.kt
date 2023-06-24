package com.jayson.komm.common.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jayson.komm.common.R
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
        private const val VIEW_TYPE_LOAD_MORE = 1
    }

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerView.Adapter<*>
    private var dataList: MutableList<T> = mutableListOf()
    private var isRefreshing = false
    private var isLoading = false

    // 是否可加载
    private var isEnableLoading = true

    // 底部加载更多类型
    private var currentLoadMoreState = LoadMoreType.LOAD_MORE

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_base_list, container, false)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        recyclerView = view.findViewById(R.id.recycler_view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupInit()
        swipeRefreshLayout.setOnRefreshListener {
            loadData()
        }
        adapter = MoreListAdapter()
        recyclerView.let {
            it.itemAnimator = null
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (isEnableLoading && !recyclerView.canScrollVertically(1)) {
                        // 滑动到底部，触发“加载更多”操作
                        currentLoadMoreState = LoadMoreType.LOAD_MORE
                        recyclerView.postDelayed({
                            loadMoreData()
                        }, 500)
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
    abstract fun bindDataViewHolder(holder: RecyclerView.ViewHolder, data: T?)

    /**
     * 设置不可刷新或加载更多
     */
    open fun setRefreshOrLoadEnable(refreshEnable: Boolean = true, loadEnable: Boolean = true) {
        swipeRefreshLayout.isEnabled = refreshEnable
        isEnableLoading = loadEnable
        if (!loadEnable) {
            currentLoadMoreState = LoadMoreType.HIDE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadData() {
        isRefreshing = true
        lifecycleScope.launch(Dispatchers.IO) {
            getListData()?.let {
                dataList.clear()
                dataList.addAll(it)
                withContext(Dispatchers.Main) {
                    if (it.isEmpty()) {
                        noData(0)
                    }
                    adapter.notifyDataSetChanged()
                    swipeRefreshLayout.isRefreshing = false
                    isRefreshing = false
                }
            } ?: let {
                swipeRefreshLayout.isRefreshing = false
                isRefreshing = false
            }
        }
    }

    private fun loadMoreData() {
        isLoading = true
        lifecycleScope.launch(Dispatchers.IO) {
            getListData()?.let {
                val startPosition = dataList.size
                dataList.addAll(it)
                withContext(Dispatchers.Main) {
                    if (it.isEmpty()) {
                        currentLoadMoreState = LoadMoreType.NO_DATA
                        noData(1)
                    }
                    adapter.notifyItemRangeInserted(startPosition, dataList.size)
                    isLoading = false
                }
            }
        }
    }

    enum class LoadMoreType {
        LOAD_MORE,
        NO_DATA,
        HIDE
    }

    /**
     * 一个MoreListAdapter
     * --包含加载更多逻辑
     */
    inner class MoreListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        /**
         * 加载更多Holder
         */
        inner class LoadMoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bindData(loadMoreType: LoadMoreType) {
                if (dataList.size == 0) {
                    return
                }
                when (loadMoreType) {
                    LoadMoreType.LOAD_MORE -> {
                        itemView.visibility = View.VISIBLE
                        itemView.findViewById<TextView>(R.id.load_tv)?.text = "加载更多"
                    }
                    LoadMoreType.NO_DATA -> {
                        itemView.visibility = View.VISIBLE
                        itemView.findViewById<TextView>(R.id.load_tv)?.text = "到底了"
                    }
                    LoadMoreType.HIDE -> {
                        itemView.visibility = View.GONE
                    }
                }
            }
        }

        /**
         * 创建不同的holder
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (viewType == VIEW_TYPE_LOAD_MORE) {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_load_more, parent, false)
                return LoadMoreViewHolder(view)
            }
            // 子类实现特性
            return createDataViewHolder(parent)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is BaseListFragment<*>.MoreListAdapter.LoadMoreViewHolder) {
                holder.bindData(currentLoadMoreState)
            } else {
                val data = dataList[position]
                // 子类实现特性
                bindDataViewHolder(holder, data)
            }
        }

        override fun getItemViewType(position: Int): Int {
            // 没有“加载更多”栏
            return if (isShowLoadMore() && (position == itemCount - 1)) {
                VIEW_TYPE_LOAD_MORE
            } else {
                super.getItemViewType(position)
            }
        }

        override fun getItemCount(): Int {
            // 有“加载更多”栏则+1
            return if(isShowLoadMore()){ dataList.size + 1 } else dataList.size
        }

        /**
         * 是否显示“加载更多”栏
         */
        private fun isShowLoadMore(): Boolean {
            return currentLoadMoreState != LoadMoreType.HIDE
        }
    }
}
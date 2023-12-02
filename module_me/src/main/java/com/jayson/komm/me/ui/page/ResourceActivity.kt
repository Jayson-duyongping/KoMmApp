package com.jayson.komm.me.ui.page

import android.Manifest
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.lifecycleScope
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.ext.replaceFragment
import com.jayson.komm.common.util.*
import com.jayson.komm.data.bean.Classify
import com.jayson.komm.data.manager.FileDataManager
import com.jayson.komm.me.R
import com.jayson.komm.me.databinding.ActivityResourceBinding
import com.jayson.komm.me.ui.fragment.GroupDialogFragment
import com.jayson.komm.me.ui.fragment.ResourceGridFrag
import com.jayson.komm.me.ui.fragment.ResourceListFrag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*


class ResourceActivity : BaseActivity() {

    companion object {
        private const val TAG = "ResourceActivity"

        private const val manageDocumentPermission = Manifest.permission.MANAGE_DOCUMENTS
        private const val manageDocumentRequestCode = 0x00001
    }

    private lateinit var binding: ActivityResourceBinding

    private val gridFragment = ResourceGridFrag()
    private val listFragment = ResourceListFrag()

    private var scanFileDir: String? = null
    private var pathStack = Stack<String>()

    private val copyFilePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            // 处理选择的文件
            LogUtils.d(TAG, "copyFilePickerLauncher, result: $uris")
            lifecycleScope.launch {
                for (uri in uris) {
                    FileUtils.copyContentResolverUriToFileDir(
                        contentResolver,
                        uri,
                        pathStack.peek()
                    )
                }
                withContext(Dispatchers.Main) {
                    updateData(pathStack.peek(), false)
                }
            }
        }

    private val moveFilePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            // 处理选择的文件
            LogUtils.d(TAG, "moveFilePickerLauncher, result: $uris")
            lifecycleScope.launch {
                for (uri in uris) {
                    FileUtils.moveContentResolverUriToFileDir(
                        contentResolver,
                        uri,
                        pathStack.peek()
                    )
                }
                withContext(Dispatchers.Main) {
                    updateData(pathStack.peek(), false)
                }
            }
        }

    override fun initView() {
        super.initView()
        binding = ActivityResourceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        TitleBarUtils.setupWithMore(this, "资源库",
            onBackListener = {
                onBackPressed()
            },
            onMoreListener = {
                // 新增
                DialogUtils.showPopWindow(
                    this,
                    it,
                    R.layout.layout_dialog_add,
                    Pair(R.id.new_btn) {
                        showNewCreateDialog()
                    },
                    Pair(R.id.upload_btn) {
                        // 上传-copy
                        copyFilePickerLauncher.launch("*/*")
                    },
                    Pair(R.id.move_btn) {
                        // 移动-move
                        moveFilePickerLauncher.launch("*/*")
                    }
                )
            }
        )
    }

    override fun initData() {
        super.initData()
        replaceFragment(R.id.res_folder_container, gridFragment)
        replaceFragment(R.id.res_file_container, listFragment)
        scanFileDir = intent.getStringExtra(FileDataManager.INTENT_TAG)
        updateData(scanFileDir)
        // NestedScrollView滚动监听
        binding.contentSv.setOnScrollChangeListener { v: NestedScrollView, _: Int, scrollY: Int, _: Int, _: Int ->
            if (scrollY == (v.getChildAt(0).measuredHeight - v.measuredHeight)) {
                // 滚动到底部 - 执行底部加载的操作(因为会拦截recycleView的滑动到底部，所以需要主动调用)
                if (!TitleBarUtils.isEditMode(this).first) {
                    listFragment.handleLoadMore {
                        // 加载更多布局出来了之后还要滑动到最底部
                        binding.contentSv.post {
                            binding.contentSv.fullScroll(View.FOCUS_DOWN)
                        }
                    }
                }
            }
        }
    }

    private fun showContentView() {
        binding.emptyTv.visibility = View.GONE
        binding.contentSv.visibility = View.VISIBLE
    }

    private fun showEmptyView() {
        binding.contentSv.visibility = View.GONE
        binding.emptyTv.visibility = View.VISIBLE
    }

    private fun showNewCreateDialog() {
        DialogUtils.showInputDialog(
            this,
            "新建文件夹",
            onConfirmClickListener = { inputStr ->
                lifecycleScope.launch(Dispatchers.IO) {
                    if (inputStr.isEmpty()) {
                        showToast("输入为空")
                        return@launch
                    }
                    kotlin.runCatching {
                        val folder = File(pathStack.peek(), inputStr)
                        if (folder.exists()) {
                            showToast("文件已存在")
                            return@launch
                        }
                        folder.mkdirs()
                        withContext(Dispatchers.Main) {
                            ToastUtil.show(this@ResourceActivity, "新建文件夹-$inputStr")
                            updateData(pathStack.peek(), false)
                        }
                    }.onFailure {
                        LogUtils.e(TAG, "showNewCreateDialog, e: $it")
                    }
                }
            }
        )
    }

    private suspend fun showToast(message: String) {
        withContext(Dispatchers.Main) {
            ToastUtil.show(this@ResourceActivity, message)
        }
    }

    private fun setPathTitle(path: String) {
        var showPath = path
        if (path.contains(FileDataManager.resourceHidePath)) {
            showPath = path.replace(FileDataManager.resourceHidePath, "")
        }
        if (path.contains("/")) {
            showPath = showPath.replace("/", " ● ")
        }
        binding.pathTv.text = showPath
    }

    /**
     * 刷新数据及页面
     */
    fun updateData(scanPath: String?, isAdd: Boolean = true) {
        LogUtils.d(TAG, "updateData, scanPath: $scanPath")
        if (scanPath == null) {
            return
        }
        lifecycleScope.launch {
            val scanAllFiles = FileDataManager.scanCurrentFiles(File(scanPath))
            val folders = scanAllFiles.first
            val files = scanAllFiles.second
            if (isAdd) {
                pathStack.push(scanPath)
            }
            setPathTitle(scanPath)
            if ((folders.size == 0) && (files.size == 0)) {
                showEmptyView()
                return@launch
            }
            showContentView()
            binding.resFolderTv.visibility = if (folders.size != 0) View.VISIBLE else View.GONE
            binding.resFileTv.visibility = if (files.size != 0) View.VISIBLE else View.GONE
            gridFragment.updateData(folders)
            listFragment.updateData(files)
        }
    }

    /**
     * 刷新当前数据及页面
     */
    fun updateCurrentPath() {
        TitleBarUtils.handleEditMode(this@ResourceActivity)
        updateData(pathStack.peek(), false)
    }

    fun showListEditMode() {
        listFragment.apply {
            setIsNeedItemAnim(false)
            notifyItemRangeChanged()
        }
        TitleBarUtils.switchEdit(this, "请选择项目",
            onCloseListener = {
                listFragment.apply {
                    setIsNeedItemAnim(false)
                    setAllChecked(false)
                    notifyItemRangeChanged()
                }
            },
            onAllCheckedListener = {
                listFragment.apply {
                    setIsNeedItemAnim(false)
                    setAllChecked(it)
                    notifyItemRangeChanged()
                }
            },
            onEditListener = {
                val edits = listFragment.getEditingFiles()
                if (edits.isEmpty()) {
                    ToastUtil.show(this@ResourceActivity, "未选中文件")
                    return@switchEdit
                }
                DialogUtils.showPopWindow(
                    this,
                    it,
                    R.layout.layout_dialog_edit,
                    Pair(R.id.move_btn) {
                        GroupDialogFragment(scanFileDir).apply {
                            setEditClassifies(listFragment.getEditingFiles())
                            show(supportFragmentManager, "group_dialog")
                        }
                    },
                    Pair(R.id.delete_btn) {
                        lifecycleScope.launch {
                            FileDataManager.deleteFiles(edits) {
                                updateCurrentPath()
                            }
                        }
                    },
                )
            }
        )
    }

    fun showGridEditMode(editData: Classify?) {
        editData ?: return
        DialogUtils.showAlertDialog(
            this,
            R.layout.layout_dialog_edit,
            Pair(R.id.move_btn) {
                GroupDialogFragment(scanFileDir).apply {
                    setEditClassifies(listOf(editData))
                    show(supportFragmentManager, "group_dialog")
                }
            },
            Pair(R.id.delete_btn) {
                lifecycleScope.launch {
                    FileDataManager.deleteFiles(listOf(editData)) {
                        updateData(pathStack.peek(), false)
                    }
                }
            }
        )
    }

    override fun onBackPressed() {
        kotlin.runCatching {
            if (TitleBarUtils.handleEditMode(this)) {
                return
            }
            pathStack.pop()
            updateData(pathStack.peek(), false)
        }.onFailure {
            LogUtils.e(TAG, "onBackPressed, e:$it")
            super.onBackPressed()
        }
    }
}
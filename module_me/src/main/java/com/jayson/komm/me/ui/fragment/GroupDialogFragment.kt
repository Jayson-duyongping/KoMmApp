package com.jayson.komm.me.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jayson.komm.common.ext.replaceFragment
import com.jayson.komm.data.bean.Classify
import com.jayson.komm.data.manager.FileDataManager
import com.jayson.komm.me.R
import com.jayson.komm.me.ui.page.ResourceActivity
import kotlinx.coroutines.launch

class GroupDialogFragment(val scanFileDir: String?) : BottomSheetDialogFragment() {

    private var removeBtn: Button? = null
    private var resourceGroupFrag: ResourceGroupFrag? = null

    private var normalLayout: RelativeLayout? = null
    private var editLayout: RelativeLayout? = null

    private var editClassifies = mutableListOf<Classify>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_TITLE, com.jayson.komm.common.R.style.BottomSheetDialogStyle);
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        // 设置对话框的样式
        dialog.window?.setDimAmount(0.3f)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_dialog_group, container, false)
        removeBtn = view.findViewById(R.id.remove_btn)
        initTitleBar(view)
        return view
    }

    private fun initTitleBar(view: View) {
        normalLayout = view.findViewById(com.jayson.komm.common.R.id.normal_rl)
        editLayout = view.findViewById(com.jayson.komm.common.R.id.edit_rl)
        val titleTv = view.findViewById<TextView>(com.jayson.komm.common.R.id.title_tv)
        val closeIv = view.findViewById<ImageView>(com.jayson.komm.common.R.id.close_iv)
        val backIv = view.findViewById<ImageView>(com.jayson.komm.common.R.id.back_iv)
        val allCb = view.findViewById<CheckBox>(com.jayson.komm.common.R.id.all_cb)
        val editTv = view.findViewById<TextView>(com.jayson.komm.common.R.id.edit_tv)
        val editIv = view.findViewById<ImageView>(com.jayson.komm.common.R.id.edit_iv)

        editIv.visibility = View.GONE
        allCb.visibility = View.GONE
        editTv.text = "移动到目录"
        titleTv.text = "移动到目录"
        switchIndexTitle()

        closeIv.setOnClickListener {
            dismiss()
        }
        backIv.setOnClickListener {
            resourceGroupFrag?.backParentPath()
        }
    }

    private fun switchBackTitle() {
        editLayout?.visibility = View.GONE
        normalLayout?.visibility = View.VISIBLE
    }

    private fun switchIndexTitle() {
        normalLayout?.visibility = View.GONE
        editLayout?.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = (activity as ResourceActivity)
        resourceGroupFrag = ResourceGroupFrag(scanFileDir)
        resourceGroupFrag?.let {
            replaceFragment(R.id.panel_container, it)
            it.onDataPassListener = object : ResourceGroupFrag.OnDataPassListener {
                override fun onNoticePath(path: String) {
                    if (path == scanFileDir) {
                        switchIndexTitle()
                    } else {
                        switchBackTitle()
                    }
                }
            }
        }
        removeBtn?.setOnClickListener {
            val targetPath = resourceGroupFrag?.getCurrentPath() ?: return@setOnClickListener
            lifecycleScope.launch {
                FileDataManager.removeFiles(editClassifies, targetPath) {
                    // 关闭弹出，刷新页面
                    activity.updateCurrentPath()
                    dismiss()
                }
            }
        }
    }

    /**
     * 整编辑中的Classify
     */
    fun setEditClassifies(classifies: List<Classify>) {
        editClassifies.clear()
        editClassifies.addAll(classifies)
    }
}
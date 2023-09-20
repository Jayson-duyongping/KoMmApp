package com.jayson.komm.common.util

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.jayson.komm.common.R

object TitleBarUtils {

    private const val TAG = "TitleBarUtils"

    @JvmStatic
    fun setup(
        activity: FragmentActivity,
        title: String,
        onBackListener: () -> Unit
    ) {
        kotlin.runCatching {
            LogUtils.d(TAG, "setup, title:$title")
            val normalLayout = activity.findViewById<RelativeLayout>(R.id.normal_rl)
            val editLayout = activity.findViewById<RelativeLayout>(R.id.edit_rl)
            val backIv = activity.findViewById<ImageView>(R.id.back_iv)
            val titleTv = activity.findViewById<TextView>(R.id.title_tv)

            editLayout.visibility = View.GONE
            normalLayout.visibility = View.VISIBLE
            titleTv.text = title
            backIv.setOnClickListener {
                onBackListener.invoke()
            }
        }.onFailure {
            LogUtils.e(TAG, "setup, e:$it")
        }
    }

    @JvmStatic
    fun setupWithMore(
        activity: FragmentActivity,
        title: String,
        onBackListener: () -> Unit,
        onMoreListener: (View) -> Unit,
    ) {
        kotlin.runCatching {
            LogUtils.d(TAG, "setupWithMore, title:$title")
            val normalLayout = activity.findViewById<RelativeLayout>(R.id.normal_rl)
            val editLayout = activity.findViewById<RelativeLayout>(R.id.edit_rl)
            val moreIv = activity.findViewById<ImageView>(R.id.more_iv)
            val backIv = activity.findViewById<ImageView>(R.id.back_iv)
            val titleTv = activity.findViewById<TextView>(R.id.title_tv)

            editLayout.visibility = View.GONE
            normalLayout.visibility = View.VISIBLE
            titleTv.text = title
            backIv.setOnClickListener {
                onBackListener.invoke()
            }
            moreIv.visibility = View.VISIBLE
            moreIv.setOnClickListener {
                onMoreListener.invoke(it)
            }
        }.onFailure {
            LogUtils.e(TAG, "setupWithMore, e:$it")
        }
    }

    @JvmStatic
    fun setupWithSearch(
        activity: FragmentActivity,
        title: String,
        onBackListener: () -> Unit,
        onSearchListener: () -> Unit,
    ) {
        kotlin.runCatching {
            LogUtils.d(TAG, "setupWithSearch, title:$title")
            val normalLayout = activity.findViewById<RelativeLayout>(R.id.normal_rl)
            val editLayout = activity.findViewById<RelativeLayout>(R.id.edit_rl)
            val searchIv = activity.findViewById<ImageView>(R.id.search_iv)
            val backIv = activity.findViewById<ImageView>(R.id.back_iv)
            val titleTv = activity.findViewById<TextView>(R.id.title_tv)

            editLayout.visibility = View.GONE
            normalLayout.visibility = View.VISIBLE
            titleTv.text = title
            backIv.setOnClickListener {
                onBackListener.invoke()
            }
            searchIv.visibility = View.VISIBLE
            searchIv.setOnClickListener {
                onSearchListener.invoke()
            }
        }.onFailure {
            LogUtils.e(TAG, "setupWithSearch, e:$it")
        }
    }

    @JvmStatic
    fun setupWithMoreAndSearch(
        activity: FragmentActivity,
        title: String,
        onBackListener: (View) -> Unit,
        onMoreListener: (View) -> Unit,
        onSearchListener: () -> Unit,
    ) {
        kotlin.runCatching {
            LogUtils.d(TAG, "setupWithMoreAndSearch, title:$title")
            val normalLayout = activity.findViewById<RelativeLayout>(R.id.normal_rl)
            val editLayout = activity.findViewById<RelativeLayout>(R.id.edit_rl)
            val moreIv = activity.findViewById<ImageView>(R.id.more_iv)
            val searchIv = activity.findViewById<ImageView>(R.id.search_iv)
            val backIv = activity.findViewById<ImageView>(R.id.back_iv)
            val titleTv = activity.findViewById<TextView>(R.id.title_tv)

            editLayout.visibility = View.GONE
            normalLayout.visibility = View.VISIBLE
            titleTv.text = title
            backIv.setOnClickListener {
                onBackListener.invoke(it)
            }
            moreIv.visibility = View.VISIBLE
            moreIv.setOnClickListener {
                onMoreListener.invoke(it)
            }
            searchIv.visibility = View.VISIBLE
            searchIv.setOnClickListener {
                onSearchListener.invoke()
            }
        }.onFailure {
            LogUtils.e(TAG, "setupWithMoreAndSearch, e:$it")
        }
    }

    @JvmStatic
    fun switchEdit(
        activity: FragmentActivity,
        editTitle: String,
        onCloseListener: () -> Unit,
        onAllCheckedListener: (Boolean) -> Unit,
        onEditListener: (View) -> Unit,
    ) {
        kotlin.runCatching {
            LogUtils.d(TAG, "switchEdit, editTitle:$editTitle")
            val normalLayout = activity.findViewById<RelativeLayout>(R.id.normal_rl)
            val editLayout = activity.findViewById<RelativeLayout>(R.id.edit_rl)
            val allCb = activity.findViewById<CheckBox>(R.id.all_cb)
            val closeIv = activity.findViewById<ImageView>(R.id.close_iv)
            val editIv = activity.findViewById<ImageView>(R.id.edit_iv)
            val editTv = activity.findViewById<TextView>(R.id.edit_tv)

            normalLayout.visibility = View.GONE
            editLayout.visibility = View.VISIBLE
            editTv.text = editTitle
            closeIv.setOnClickListener {
                allCb.isChecked = false
                editTv.text = ""
                editLayout.visibility = View.GONE
                normalLayout.visibility = View.VISIBLE
                onCloseListener.invoke()
            }
            editIv.setOnClickListener {
                onEditListener.invoke(it)
            }
            allCb.setOnCheckedChangeListener { _, isChecked ->
                onAllCheckedListener.invoke(isChecked)
            }
        }.onFailure {
            LogUtils.e(TAG, "switchEdit, e:$it")
        }
    }

    @JvmStatic
    fun setTitleText(
        activity: FragmentActivity,
        title: String
    ) {
        kotlin.runCatching {
            LogUtils.d(TAG, "setTitleText, title:$title")
            val normalLayout = activity.findViewById<RelativeLayout>(R.id.normal_rl)
            val editLayout = activity.findViewById<RelativeLayout>(R.id.edit_rl)
            val editTv = activity.findViewById<TextView>(R.id.edit_tv)
            val titleTv = activity.findViewById<TextView>(R.id.title_tv)
            if (normalLayout.visibility == View.VISIBLE) {
                titleTv.text = title
            }
            if (editLayout.visibility == View.VISIBLE) {
                editTv.text = title
            }
        }.onFailure {
            LogUtils.e(TAG, "setTitleText, e:$it")
        }
    }

    /**
     * 是否是编辑模式
     */
    @JvmStatic
    fun isEditMode(activity: FragmentActivity): Pair<Boolean, Boolean> {
        return kotlin.runCatching {
            val editLayout = activity.findViewById<RelativeLayout>(R.id.edit_rl)
            val allCb = activity.findViewById<CheckBox>(R.id.all_cb)
            Pair(editLayout.visibility == View.VISIBLE, allCb.isChecked)
        }.getOrElse {
            LogUtils.e(TAG, "isEditMode, e:$it")
            Pair(false, false)
        }
    }

    /**
     * 处理编辑模式，转成正常模式
     */
    @JvmStatic
    fun handleEditMode(activity: FragmentActivity): Boolean {
        return kotlin.runCatching {
            LogUtils.d(TAG, "handleEditMode")
            val editLayout = activity.findViewById<RelativeLayout>(R.id.edit_rl)
            val closeIv = activity.findViewById<ImageView>(R.id.close_iv)
            if (editLayout.visibility == View.VISIBLE) {
                closeIv.performClick()
                return true
            }
            false
        }.getOrElse {
            LogUtils.e(TAG, "handleEditMode, e:$it")
            false
        }
    }
}
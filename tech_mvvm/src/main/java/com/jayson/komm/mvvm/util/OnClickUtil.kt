package com.jayson.komm.mvvm.util

import com.jayson.komm.common.util.LogUtils

/**
 * @Author: Jayson
 * @CreateDate: 2023/3/5 10:24
 * @Version: 1.0
 * @Description:
 */
class OnClickUtil {

    companion object {
        private const val TAG = "OnClickUtil"
    }

    fun onClickWithMe() {
        LogUtils.d(TAG, "调用onClickWithMe")
        //Toast.makeText(view.context, "调用onClickWithMe", Toast.LENGTH_SHORT).show();
    }
}
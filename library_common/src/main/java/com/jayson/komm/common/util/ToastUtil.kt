package com.jayson.komm.common.util

import android.content.Context
import android.widget.Toast

object ToastUtil {

    @JvmStatic
    fun show(context: Context?, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
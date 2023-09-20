package com.jayson.komm.common.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.jayson.komm.common.R
import com.jayson.komm.common.bean.FileInfo
import com.jayson.komm.common.view.page.ShowMediaActivity
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf

/**
 * @Author: Jayson
 * @CreateDate: 2023/4/8 19:09
 * @Version: 1.0
 * @Description:
 */
object JumpUtils {

    private const val TAG = "JumpUtils"

    @JvmStatic
    fun startGoAction(activity: Activity?, intent: Intent) {
        kotlin.runCatching {
            activity?.apply {
                startActivity(intent)
                overridePendingTransition(R.anim.slide_enter_in_right, R.anim.slide_enter_out_left)
            }
        }.onFailure {
            LogUtils.e(TAG, "startGoAction, e:${it.message}")
        }
    }

    @JvmStatic
    fun startGoActivity(activity: Activity?, intent: Intent) {
        kotlin.runCatching {
            activity?.apply {
                startActivity(intent)
                overridePendingTransition(R.anim.slide_enter_in_right, R.anim.slide_enter_out_left)
            }
        }.onFailure {
            LogUtils.e(TAG, "startGoActivity, e:${it.message}")
        }
    }

    /**
     * 应用信息界面
     * @param activity
     */
    @JvmStatic
    fun goApplicationInfo(activity: Activity) {
        val localIntent = Intent()
        localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
        localIntent.data = Uri.fromParts("package", activity.packageName, null)
        activity.startActivity(localIntent)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @JvmStatic
    fun goShowMediaActivity(activity: Activity?, position: Int, fileList: List<FileInfo>) {
        kotlin.runCatching {
            val intent = Intent(activity, ShowMediaActivity::class.java).apply {
                putExtra(ShowMediaActivity.INTENT_POSITION, position)
                // 将其序列化为字节数组,将字节数组添加到Intent中
                val fileArray = ProtoBuf.encodeToByteArray(fileList)
                putExtra(ShowMediaActivity.INTENT_FILES, fileArray)
            }
            activity?.startActivity(intent)
        }.onFailure {
            LogUtils.e(TAG, "goShowMediaActivity, e:${it.message}")
        }
    }
}
package com.jayson.komm.common.util

import android.Manifest
import android.content.pm.PackageManager
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

object PermissionUtils {

    /**
     * 使用之前先授权管理所有文件权限（操作文件，类似文件管理器，需要用户手动设置）
     * 对应权限android.permission.MANAGE_EXTERNAL_STORAGE
     */
    @JvmStatic
    fun isManageAllFilesAccessPermission() = Environment.isExternalStorageManager()

    /**
     * 请求存储权限
     */
    @JvmStatic
    fun requestStoragePermissions(
        activity: AppCompatActivity,
        grantedAction: () -> Unit,
        deniedAction: () -> Unit
    ) {
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val isGranted = permissions.all {
            ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
        }
        if (isGranted) {
            grantedAction.invoke()
        } else {
            val requestPermissionLauncher = activity.registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                val isAllGranted = permissions.all { it.value }
                if (isAllGranted) {
                    grantedAction.invoke()
                } else {
                    deniedAction.invoke()
                }
            }
            requestPermissionLauncher.launch(permissions)
        }
    }
}
package com.jayson.komm.me.ui.page

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.ext.context
import com.jayson.komm.common.ext.replaceFragment
import com.jayson.komm.common.util.JumpUtils
import com.jayson.komm.common.util.ToastUtil
import com.jayson.komm.me.MeFragment
import com.jayson.komm.me.R
import com.jayson.komm.me.databinding.ActivityLocalFolderBinding
import com.jayson.komm.me.ui.fragment.FolderListFrag


class LocalFolderActivity : BaseActivity() {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 0x000123
    }

    private lateinit var binding: ActivityLocalFolderBinding

    private var typeName: String? = null

    override fun initView() {
        super.initView()
        binding = ActivityLocalFolderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initData() {
        super.initData()
        typeName = intent.getStringExtra(MeFragment.FILE_TYPE)
        // 检查权限是否已授予
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // 已授予权限，执行图片扫描和展示操作
            replaceFragment(R.id.file_container, FolderListFrag(), typeName ?: "")
        } else {
            // 未授予权限，请求权限
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    // 处理权限请求结果
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 已授予权限，执行图片扫描和展示操作
                replaceFragment(R.id.file_container, FolderListFrag(), typeName ?: "")
            } else {
                // 权限被拒绝，可以在这里处理拒绝权限的逻辑
                ToastUtil.show(context, "未授权，不能使用此功能，请先授权")
                JumpUtils.goApplicationInfo(this)
                finish()
            }
        }
    }
}
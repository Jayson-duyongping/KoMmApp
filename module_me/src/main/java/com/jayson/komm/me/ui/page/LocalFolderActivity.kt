package com.jayson.komm.me.ui.page

import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.ext.replaceFragment
import com.jayson.komm.me.MeFragment
import com.jayson.komm.me.R
import com.jayson.komm.me.databinding.ActivityLocalFolderBinding
import com.jayson.komm.me.ui.fragment.FolderListFrag


class LocalFolderActivity : BaseActivity() {

    companion object {
        private const val TAG = "LocalFolderActivity"
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
        // 已授予权限，执行图片扫描和展示操作
        replaceFragment(R.id.file_container, FolderListFrag(), typeName ?: "")
    }
}
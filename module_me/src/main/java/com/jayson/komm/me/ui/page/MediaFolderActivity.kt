package com.jayson.komm.me.ui.page

import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.ext.replaceFragment
import com.jayson.komm.common.util.TitleBarUtils
import com.jayson.komm.me.MeFragment
import com.jayson.komm.me.R
import com.jayson.komm.me.databinding.ActivityMediaFolderBinding
import com.jayson.komm.me.ui.fragment.MediaFolderListFrag


class MediaFolderActivity : BaseActivity() {

    companion object {
        private const val TITLE_NAME = "媒体库"
    }

    private lateinit var binding: ActivityMediaFolderBinding

    private var typeName: String? = null

    override fun initView() {
        super.initView()
        binding = ActivityMediaFolderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initData() {
        super.initData()
        typeName = intent.getStringExtra(MeFragment.FILE_TYPE)
        TitleBarUtils.setup(this, "$TITLE_NAME - $typeName",
            onBackListener = {
                onBackPressed()
            }
        )
        // 已授予权限，执行媒体扫描和展示操作
        replaceFragment(R.id.file_container, MediaFolderListFrag(), typeName ?: "")
    }
}
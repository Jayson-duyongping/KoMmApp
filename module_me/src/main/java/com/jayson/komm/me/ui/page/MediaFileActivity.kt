package com.jayson.komm.me.ui.page

import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.bean.FileInfo
import com.jayson.komm.common.ext.replaceFragment
import com.jayson.komm.common.util.TitleBarUtils
import com.jayson.komm.me.MeFragment
import com.jayson.komm.me.R
import com.jayson.komm.me.databinding.ActivityMediaFileBinding
import com.jayson.komm.me.ui.fragment.MediaFileListFrag
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf

@OptIn(ExperimentalSerializationApi::class)
class MediaFileActivity : BaseActivity() {

    private lateinit var binding: ActivityMediaFileBinding

    private var type: String? = null

    override fun initView() {
        super.initView()
        binding = ActivityMediaFileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        type = intent.getStringExtra(MeFragment.FILE_TYPE)
        TitleBarUtils.setup(this, "文件列表 - $type",
            onBackListener = {
                onBackPressed()
            }
        )
        val fileListFrag = MediaFileListFrag()
        replaceFragment(R.id.file_container, fileListFrag, type ?: "")
        val byteArray = intent.getByteArrayExtra(MeFragment.FILE_LIST)
        val files = byteArray?.let { ProtoBuf.decodeFromByteArray<ArrayList<FileInfo>>(it) }
        files?.let {
            fileListFrag.refreshData(it)
        }
    }
}
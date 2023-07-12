package com.jayson.komm.me.ui.page

import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.bean.FileInfo
import com.jayson.komm.common.ext.replaceFragment
import com.jayson.komm.me.MeFragment
import com.jayson.komm.me.R
import com.jayson.komm.me.databinding.ActivityLocalFileBinding
import com.jayson.komm.me.ui.fragment.FileListFrag
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf

@OptIn(ExperimentalSerializationApi::class)
class LocalFileActivity : BaseActivity() {

    private lateinit var binding: ActivityLocalFileBinding

    private var typeName: String? = null

    override fun initView() {
        super.initView()
        binding = ActivityLocalFileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val fileListFrag = FileListFrag()
        replaceFragment(R.id.file_container, fileListFrag, typeName ?: "")
        val byteArray = intent.getByteArrayExtra(MeFragment.FILE_LIST)
        val files = byteArray?.let { ProtoBuf.decodeFromByteArray<ArrayList<FileInfo>>(it) }
        files?.let {
            fileListFrag.refreshData(it)
        }
    }
}
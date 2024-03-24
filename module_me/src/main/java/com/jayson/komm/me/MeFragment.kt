package com.jayson.komm.me

import android.content.Intent
import android.view.View
import com.jayson.komm.common.base.BaseFragment
import com.jayson.komm.common.util.JumpUtils.startGoAction
import com.jayson.komm.common.util.JumpUtils.startGoActivity
import com.jayson.komm.data.manager.FileDataManager
import com.jayson.komm.me.databinding.FragmentMeBinding
import com.jayson.komm.me.ui.page.MediaFolderActivity
import com.jayson.komm.me.ui.page.ResourceActivity

class MeFragment : BaseFragment() {

    companion object {
        const val FILE_TYPE = "fileType"
        const val TYPE_IMAGE = "Image"
        const val TYPE_VIDEO = "Video"
        const val TYPE_AUDIO = "Audio"
        const val FILE_LIST = "fileList"

        private const val ACTION_MVVM = "com.jayson.komm.mvvm.Main"
        private const val ACTION_API = "com.jayson.komm.api.Main"
        private const val ACTION_DEV = "com.jayson.komm.dev.Main"
        private const val ACTION_UI = "com.jayson.komm.ui.Main"
    }

    private lateinit var binding: FragmentMeBinding

    override fun getLayoutRes(): Int {
        return R.layout.fragment_me
    }

    override fun initView(view: View) {
        super.initView(view)
        binding = FragmentMeBinding.bind(view)
        binding.mvvmBtn.setOnClickListener {
            startGoAction(activity, Intent(ACTION_MVVM))
        }
        binding.apiBtn.setOnClickListener {
            startGoAction(activity, Intent(ACTION_API))
        }
        binding.devBtn.setOnClickListener {
            startGoAction(activity, Intent(ACTION_DEV))
        }
        binding.uiBtn.setOnClickListener {
            startGoAction(activity, Intent(ACTION_UI))
        }
        binding.pictureBtn.setOnClickListener {
            goLocalFileActivity(TYPE_IMAGE)
        }
        binding.videoBtn.setOnClickListener {
            goLocalFileActivity(TYPE_VIDEO)
        }
        binding.audioBtn.setOnClickListener {
            goLocalFileActivity(TYPE_AUDIO)
        }
        binding.resourceBtn.setOnClickListener {
            Intent(activity, ResourceActivity::class.java).apply {
                putExtra(FileDataManager.INTENT_TAG, FileDataManager.resourceMaHidePath)
                startGoActivity(activity, this)
            }
        }
        binding.resourceMmBtn.setOnClickListener {
            Intent(activity, ResourceActivity::class.java).apply {
                putExtra(FileDataManager.INTENT_TAG, FileDataManager.resourceMsHidePath)
                startGoActivity(activity, this)
            }
        }
    }

    private fun goLocalFileActivity(typeName: String) {
        val intent = Intent(activity, MediaFolderActivity::class.java).apply {
            putExtra(FILE_TYPE, typeName)
        }
        startGoActivity(activity, intent)
    }
}
package com.jayson.komm.me

import android.content.Intent
import android.view.View
import com.jayson.komm.common.base.BaseFragment
import com.jayson.komm.common.ext.addClickScale
import com.jayson.komm.common.util.JumpUtils.startGoAction
import com.jayson.komm.common.util.JumpUtils.startGoActivity
import com.jayson.komm.me.databinding.FragmentMeBinding
import com.jayson.komm.me.ui.page.LocalFolderActivity

class MeFragment : BaseFragment() {

    companion object {
        const val FILE_TYPE = "fileType"
        const val TYPE_PICTURE = "picture"
        const val TYPE_VIDEO = "video"
        const val TYPE_AUDIO = "audio"
        const val FILE_LIST = "fileList"

        private const val ACTION_MVVM = "com.jayson.komm.mvvm.Main"
        private const val ACTION_MM = "com.jayson.komm.api.Main"
    }

    private lateinit var binding: FragmentMeBinding

    override fun getLayoutRes(): Int {
        return R.layout.fragment_me
    }

    override fun initView(view: View) {
        super.initView(view)
        binding = FragmentMeBinding.bind(view)
        binding.mvvmBtn.addClickScale { startGoAction(activity, Intent(ACTION_MVVM)) }
        binding.apiBtn.addClickScale { startGoAction(activity, Intent(ACTION_MM)) }
        binding.pictureBtn.addClickScale {
            goLocalFileActivity(TYPE_PICTURE)
        }
        binding.videoBtn.addClickScale {
            goLocalFileActivity(TYPE_VIDEO)
        }
        binding.audioBtn.addClickScale {
            goLocalFileActivity(TYPE_AUDIO)
        }
    }

    private fun goLocalFileActivity(typeName: String) {
        val intent = Intent(activity, LocalFolderActivity::class.java).apply {
            putExtra(FILE_TYPE, typeName)
        }
        startGoActivity(activity, intent)
    }
}
package com.jayson.komm.me.ui.page

import android.content.Intent
import android.os.Environment
import android.provider.Settings
import androidx.lifecycle.lifecycleScope
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.util.LogUtils
import com.jayson.komm.common.util.ToastUtil
import com.jayson.komm.me.databinding.ActivityResourceBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class ResourceActivity : BaseActivity() {

    companion object {
        private const val TAG = "ResourceActivity"

        private const val RESOURCE_FILE_NAME = "KommResource"
        private const val RESOURCE_HIDE_FILE_NAME = ".KommResource"
        private const val SEPARATOR = "/"
        private const val NOME_DIA = ".nomedia"
    }

    private val resourcePath =
        Environment.getExternalStorageDirectory().path + SEPARATOR + RESOURCE_FILE_NAME
    private val resourceHidePath =
        Environment.getExternalStorageDirectory().path + SEPARATOR + RESOURCE_HIDE_FILE_NAME

    private lateinit var binding: ActivityResourceBinding

    override fun initView() {
        super.initView()
        binding = ActivityResourceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.showBtn.setOnClickListener {
            // 显示
            showFile()
        }
        binding.hideBtn.setOnClickListener {
            // 隐藏
            hideFile()
        }
    }

    override fun initData() {
        super.initData()
        if (!Environment.isExternalStorageManager()) {
            val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            startActivity(intent)
            return
        }
        scanImageFiles(File(resourceHidePath))
    }

    private fun scanImageFiles(folder: File) {
        lifecycleScope.launch(Dispatchers.IO) {
            val files = folder.listFiles() ?: return@launch
            for (file in files) {
                if (file.isDirectory) {
                    // 递归遍历子文件夹
                    scanImageFiles(file)
                    LogUtils.d(TAG, "scanImageFiles, isDirectory:${file.name}")
                } else {
                    // 判断文件是否为图片文件
                    if (isImageFile(file)) {
                        // 扫描到图片文件
                        LogUtils.d(TAG, "scanImageFiles, Image File:${file.parentFile?.name},${file.absolutePath}")
                        // 进行相应操作
                    }
                }
            }
        }
    }

    private fun isImageFile(file: File): Boolean {
        val name = file.name
        val extension = name.substringAfterLast(".", "")
        val imageExtensions = arrayOf("jpg", "jpeg", "png", "gif", "bmp")
        for (imageExtension in imageExtensions) {
            if (extension.equals(imageExtension, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    private fun showFile() {
        lifecycleScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                File(resourceHidePath).let {
                    // 删除之前的名为".nomedia"的空文件
                    File(it, NOME_DIA).delete()
                    // 从之前的.XXX改回XXX
                    val newName: String = it.name.substring(1)
                    val newFile = File(it.parentFile, newName)
                    it.renameTo(newFile)
                }
                withContext(Dispatchers.Main) {
                    ToastUtil.show(this@ResourceActivity, "文件已经设置为可显示")
                }
            }.onFailure {
                LogUtils.e(TAG, "showFile, e:$it")
            }
        }
    }

    private fun hideFile() {
        lifecycleScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                File(resourcePath).let {
                    // 指定文件夹中创建一个名为".nomedia"的空文件，这会告诉系统该文件夹中不应该被媒体库扫描
                    File(it, NOME_DIA).createNewFile()
                    // 改名为.XXX实现隐藏
                    val newName = "." + it.name
                    val newFile = File(it.parentFile, newName)
                    it.renameTo(newFile)
                }
                withContext(Dispatchers.Main) {
                    ToastUtil.show(this@ResourceActivity, "文件已经设置为隐藏")
                }
            }.onFailure {
                LogUtils.e(TAG, "hideFile, e:$it")
            }
        }
    }
}
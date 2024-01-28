package com.jayson.komm.api.view

import android.Manifest
import android.R
import android.app.Dialog
import android.content.pm.PackageManager
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.jayson.komm.api.databinding.ActivityWebBinding
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.databinding.LayoutMediaBinding
import com.jayson.komm.common.util.DownLoadUtils.downloadImage
import com.jayson.komm.common.util.LogUtils
import com.jayson.komm.common.view.custom.FlowLayout

class WebActivity : BaseActivity() {

    companion object {
        private const val TAG = "WebActivity"
        private const val REQUEST_WRITE_EXTERNAL_STORAGE = 1
        private const val ANDROID_URL = "https://developer.android.google.cn/guide?hl=zh-cn"
        private const val JUEJIN_URL = "https://juejin.cn/android"
        private val tags = listOf(
            FlowLayout.Tag(name = "开发者平台", linkUrl = ANDROID_URL),
            FlowLayout.Tag(name = "掘金", linkUrl = JUEJIN_URL)
        )
    }

    private lateinit var binding: ActivityWebBinding
    private var goBackFlag = false

    override fun initView() {
        super.initView()
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTagView()
        setWebView()
    }

    private fun setTagView() {
        binding.flowLayout.addTags(tags, object : FlowLayout.OnTagClickListener {
            override fun onTagClick(tag: FlowLayout.Tag) {
                binding.customWebView.loadUrl(tag.linkUrl)
            }
        })
    }

    private fun setWebView() {
        binding.customWebView.apply {
            registerForContextMenu(this)
        }
        binding.customWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                LogUtils.d(TAG, "setWebView, shouldOverrideUrlLoading-${request.url}")
                // 加载
                goBackFlag = false
                view.loadUrl(request.url.toString())
                return true
            }
        }
        binding.customWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                LogUtils.d(TAG, "setWebView, onProgressChanged-$newProgress")
                // 返回时不会加载页面，但也有进度条，去掉进度条
                if (goBackFlag) {
                    return
                }
                // 进度条
                binding.webProgressBar.apply {
                    visibility = View.VISIBLE
                    progress = newProgress
                    if (newProgress == 100) {
                        visibility = View.GONE
                        goBackFlag = false
                    }
                }
            }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        if (webImageUrl().isNotEmpty()) {
            menu?.add(0, 1, 0, "查看图片")
            menu?.add(0, 2, 0, "保存图片")
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        webImageUrl().let {
            if (it.isNotEmpty()) {
                when (item.itemId) {
                    1 -> {
                        viewPicture(it)
                    }
                    2 -> {
                        downPicture(it)
                        return true
                    }
                }
            }
        }
        return super.onContextItemSelected(item)
    }

    /**
     * 申请权限结果
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadImage(this@WebActivity, webImageUrl())
                } else {
                    Toast.makeText(this, "需要存储权限才能下载图片", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    /**
     * 是图片类型则返回imgStr
     */
    private fun webImageUrl(): String {
        // WebView-HitTestResult可以识别当前触碰地方的类型，可以满足长按文本选中、长按图片保存等功能。
        val hitTestResult = binding.customWebView.hitTestResult
        if (hitTestResult.type == WebView.HitTestResult.IMAGE_TYPE
            || hitTestResult.type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE
        ) {
            return hitTestResult.extra ?: ""
        }
        return ""
    }

    /**
     * 查看图片
     */
    private fun viewPicture(imageUrl: String) {
        val dialog = Dialog(this, R.style.Theme_Black_NoTitleBar_Fullscreen)
        val photoBinding = LayoutMediaBinding.inflate(layoutInflater)
        dialog.setContentView(photoBinding.root)
        Glide.with(this)
            .load(imageUrl)
            .into(photoBinding.imagePv)
        dialog.show()
    }

    /**
     * 下载图片
     */
    private fun downPicture(imageUrl: String) {
        // 下载图片时检查存储权限
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_EXTERNAL_STORAGE
            )
        } else {
            downloadImage(this, imageUrl)
        }
    }

    /**
     * 返回键处理，如果WebView可以返回则返回，否则退出Activity
     */
    override fun onBackPressed() {
        if (binding.customWebView.canGoBack()) {
            binding.customWebView.goBack()
            goBackFlag = true
        } else {
            super.onBackPressed()
        }
    }
}
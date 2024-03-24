package com.jayson.komm.api.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.jayson.komm.api.R
import com.jayson.komm.api.databinding.ActivityPythonBinding
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.util.ToastUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

@SuppressLint("UseCompatLoadingForDrawables")
class PythonActivity : BaseActivity() {

    companion object {
        private const val TAG = "PythonActivity"
    }

    private lateinit var binding: ActivityPythonBinding

    private val originBitmap by lazy {
        (getDrawable(R.mipmap.placeholder) as BitmapDrawable).bitmap
    }

    private val python by lazy {
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
        Python.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPythonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.pictureIv.setImageBitmap(originBitmap)
        binding.accessPythonBtn.setOnClickListener {
            accessPython()
        }
        binding.resizeBtn.setOnClickListener {
            resizeBitmap()
        }
    }

    private fun accessPython() {
        val pyObject = python.getModule("hello")
        val pythonResult = pyObject.callAttr("sayHello").toString()
        ToastUtil.show(this, pythonResult)
    }

    private fun resizeBitmap() {
        lifecycleScope.launch(Dispatchers.IO) {
            // 将Bitmap转换为字节数组
            val stream = ByteArrayOutputStream()
            originBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray: ByteArray = stream.toByteArray()
            // 调用Python脚本，传递Bitmap数据并接收返回的处理后的结果
            val pyObject = python.getModule("hello")
            val pythonResult: PyObject = pyObject.callAttr("getResizeBitmap", byteArray, 200, 150)
            // 解析Python返回的处理后的Bitmap数据
            val resizedByteArray: ByteArray = pythonResult.toJava(ByteArray::class.java)
            val resizedBitmap: Bitmap =
                BitmapFactory.decodeByteArray(resizedByteArray, 0, resizedByteArray.size)
            withContext(Dispatchers.Main) {
                binding.pictureIv.setImageBitmap(resizedBitmap)
            }
        }
    }
}
package com.jayson.komm.dev.view

import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.dev.databinding.ActivityPopWindowBinding
import com.jayson.komm.dev.view.popwindow.BlurPopupWindow

class PopWindowActivity : BaseActivity() {

    companion object {
        private const val TAG = "PopWindowActivity"

    }

    private lateinit var binding: ActivityPopWindowBinding

    private var blurPopup: BlurPopupWindow? = null

    override fun initView() {
        super.initView()

        // 初始化binding
        binding = ActivityPopWindowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        blurPopup = BlurPopupWindow(this)
        binding.popBtn.setOnClickListener {
            blurPopup?.showBelow(it)
        }

        setupTextWatcher()
    }


    private fun setupTextWatcher() {
        binding.etInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.toString().isNotBlank()) {
                        blurPopup?.apply {
                            setContent(it.toString())
                            if (!isShowing) {
                                showAsDropDown(binding.etInput, 0, 0, Gravity.START)
                            }
                        }
                    } else {
                        blurPopup?.dismiss()
                    }
                }
            }
        })
    }
}
package com.jayson.komm.login.view

import android.widget.Toast
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.common.ext.immediateStatusBar
import com.jayson.komm.login.contact.LoginContact.*
import com.jayson.komm.login.databinding.ActivityLoginBinding
import com.jayson.komm.login.presenter.LoginPresenter


/**
 * @Author: Jayson
 * @CreateDate: 2022/12/11 22:52
 * @Version: 1.0
 * @Description: 类，实现ILoginView接口，显示界面
 * V 调用 P，P 操作 V，P 调用 M，M 回调给 P
 */
class LoginActivity : BaseActivity(), ILoginView {
    private lateinit var binding: ActivityLoginBinding

    private var mLoginPresenter: LoginPresenter? = null

    override fun setContentView() {
        super.setContentView()
        immediateStatusBar(true)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initView() {
        super.initView()
        setPaddingStatusBar(binding.rootLl)
    }

    override fun initData() {
        super.initData()
        mLoginPresenter = LoginPresenter(this)
        binding.loginBtn.setOnClickListener {
            mLoginPresenter?.login(
                binding.nameEt.text.toString(),
                binding.passwordEt.text.toString()
            )
        }
    }

    override fun showToastFailed() {
        Toast.makeText(this, "登陆失败", Toast.LENGTH_LONG).show();
    }

    override fun showToastSuccess() {
        Toast.makeText(this, "登陆成功", Toast.LENGTH_LONG).show();
    }
}
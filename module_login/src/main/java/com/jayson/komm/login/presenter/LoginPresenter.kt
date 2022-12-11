package com.jayson.komm.login.presenter

import com.jayson.komm.login.contact.LoginContact.*
import com.jayson.komm.login.model.LoginModel


/**
 * @Author: Jayson
 * @CreateDate: 2022/12/11 22:52
 * @Version: 1.0
 * @Description: 类，衔接M和V并控制逻辑
 */
class LoginPresenter(loginView: ILoginView?) : LoginCallBack {

    private val mLoginView: ILoginView? = loginView
    private val mLoginModel: ILoginModel? by lazy { LoginModel(this) }

    override fun success() {
        mLoginView?.showToastSuccess()
    }

    override fun failed() {
        mLoginView?.showToastFailed()
    }

    fun login(name: String, password: String) {
        mLoginModel?.login(name, password);
    }
}
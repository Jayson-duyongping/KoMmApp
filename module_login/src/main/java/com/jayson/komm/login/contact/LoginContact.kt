package com.jayson.komm.login.contact

/**
 * @Author: Jayson
 * @CreateDate: 2022/12/11 23:34
 * @Version: 1.0
 * @Description: MVP的契约者（Contact）主要统一了接口管理
 */
class LoginContact {
    /**
     * 接口，定义model中需要使用的方法
     */
    interface ILoginModel {
        fun login(name: String?, password: String?)
    }

    /**
     * 接口，定义界面中需要使用的方法
     */
    interface ILoginView {
        fun showToastFailed()
        fun showToastSuccess()
    }

    /**
     * 接口，处理网络请求的回调的接口
     */
    interface LoginCallBack {
        fun success()
        fun failed()
    }
}
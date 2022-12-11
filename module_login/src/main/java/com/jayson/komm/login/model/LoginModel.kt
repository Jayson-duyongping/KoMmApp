package com.jayson.komm.login.model

import com.jayson.komm.login.contact.LoginContact.*

/**
 * @Author: Jayson
 * @CreateDate: 2022/12/11 22:51
 * @Version: 1.0
 * @Description: 类，实现ILoginModel接口，处理网络和数据持久化
 */
class LoginModel(callBack: LoginCallBack?) : ILoginModel {

    private val mCallBack: LoginCallBack? = callBack

    override fun login(name: String?, password: String?) {
        //假设请求了网络, 假设用户名和密码一样为校验成功; 如果是异步网络请求，再写个接口回调
        if (name.equals(password)) {
            mCallBack?.success();
        } else {
            mCallBack?.failed();
        }
    }
}
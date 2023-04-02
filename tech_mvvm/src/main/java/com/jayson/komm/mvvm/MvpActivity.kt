package com.jayson.komm.mvvm

import android.widget.Toast
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.mvvm.databinding.ActivityMvpBinding

/**
 * MVP最佳实践
 * 在Android Kotlin MVP的实践中，一般会将业务逻辑和数据的处理放在Presenter中，将界面的展示和交互放在View中。以下是一个最佳实践的示例代码：
 */
class MvpActivity : BaseActivity(), IUserView {

    private lateinit var binding: ActivityMvpBinding

    // 创建Presenter对象
    private lateinit var presenter: UserPresenter

    override fun initView() {
        super.initView()
        binding = ActivityMvpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = UserPresenter(this)
    }

    override fun initData() {
        super.initData()
        binding.userBtn.setOnClickListener {
            // 调用Presenter的业务逻辑方法
            presenter.getUser(5)
        }
    }

    override fun showUser(user: UserModel) {
        // 更新界面显示
        binding.userNameTv.text = user.name
        binding.userAgeTv.text = user.age.toString()
    }

    override fun showError(error: String) {
        // 显示错误信息
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

}

/**
 * Model
 * Model层主要用于封装数据和业务逻辑，可以使用Kotlin类或者接口来实现。以下是一个Model类的示例代码：
 */
data class UserModel(val name: String, val age: Int)

/**
 * View
 * View层主要负责用户界面的展示和交互，可以使用Activity、Fragment或者自定义View来实现。以下是一个View接口的示例代码：
 */
interface IUserView {
    fun showUser(user: UserModel)
    fun showError(error: String)
}

/**
 * Presenter
 * Presenter层主要用于处理用户界面和数据之间的交互，实现业务逻辑的处理和数据的更新。以下是一个Presenter类的示例代码：
 */
class UserPresenter(private val view: IUserView) {
    fun getUser(userId: Int) {
        // 从Model层获取用户数据
        val user = UserModelManager.getUser(userId)
        if (user != null) {
            // 更新View层的显示
            view.showUser(user)
        } else {
            // 显示错误信息到View层
            view.showError("Failed to get user data!")
        }
    }
}

object UserModelManager {
    fun getUser(userId: Int): UserModel {
        return UserModel("ZhangSan", 20 + userId)
    }
}


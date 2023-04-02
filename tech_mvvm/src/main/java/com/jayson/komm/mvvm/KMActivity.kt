package com.jayson.komm.mvvm

import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jayson.komm.common.base.BaseActivity
import com.jayson.komm.mvvm.databinding.ActivityKmBinding

/**
 * MVVM最佳实践
 * MVVM是一种架构模式，它将应用程序分为三个部分：模型（Model）、视图（View）和视图模型（ViewModel）。
 * 模型负责数据的获取和处理，
 * 视图负责展示数据和与用户交互，
 * 视图模型负责处理UI逻辑和数据之间的交互，实现业务逻辑的处理和数据的更新。
 * Databinding是一种用于绑定UI组件和数据的技术，通过在XML布局文件中使用表达式来绑定数据和视图，可以减少代码量和提高代码的可读性。
 */

/**
 * View层：
 * View层同样负责用户界面的展示和交互。在这个示例中我们创建一个UserActivity类，用于展示用户的数据。在布局文件中，我们使用了databinding来绑定ViewModel中的数据。
 */
class KMActivity : BaseActivity() {

    private lateinit var viewModel: UserViewModel
    private lateinit var binding: ActivityKmBinding

    override fun initView() {
        super.initView()
        // 初始化binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_km)
        // 创建ViewModel对象
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        // 观察ViewModel中的数据变化
        viewModel.user.observe(this, Observer { user ->
            binding.user = user
        })
        viewModel.error.observe(this, Observer { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        })
        // 调用ViewModel的业务逻辑方法
        binding.onClickListener = View.OnClickListener {
            viewModel.getUser(5)
        }
    }
}

/**
 * Model层：
 * Model层同样负责数据的获取和处理，以及业务逻辑的实现。在这个示例中我们创建一个UserModel类，用于表示一个用户的数据。
 */
data class MUserModel(val name: String, val age: Int)

/**
 * ViewModel层：
 * ViewModel层同样负责处理UI逻辑和数据之间的交互，实现业务逻辑的处理和数据的更新。在这个示例中我们创建一个UserViewModel类，用于处理用户数据的获取和更新。
 */
class UserViewModel : ViewModel() {
    private val repository = UserRepository()
    val user = MutableLiveData<MUserModel>()
    val error = MutableLiveData<String>()
    fun getUser(userId: Int) {
        // 从Repository层获取用户数据
        repository.getUser(userId, object : UserCallback {
            override fun onSuccess(user: MUserModel?) {
                // 更新ViewModel中的数据
                this@UserViewModel.user.value = user
            }

            override fun onError(error: String?) {
                // 更新ViewModel中的错误信息
                this@UserViewModel.error.value = error
            }
        })
    }
}

/**
 * Repository层：
 * Repository层同样负责访问远程数据源或本地数据库，并将数据转换为Model对象。在这个示例中我们创建一个UserRepository类，用于从远程数据源或本地数据库获取用户数据。
 */
class UserRepository {
    fun getUser(userId: Int, callback: UserCallback) {
        // 模拟从远程数据源或本地数据库获取用户数据
        var user: MUserModel? = null
        if (userId != 0) {
            user = MUserModel("TangSan", userId + 20)
        }
        // callback回调
        if (user != null) {
            callback.onSuccess(user)
        } else {
            callback.onError("Failed to get user data!")
        }
    }
}

/**
 * Callback接口：
 * Callback接口同样用于在异步任务完成后回调，通知ViewModel层更新数据。在这个示例中我们创建了一个UserCallback接口，用于回调获取用户数据的结果。
 */
interface UserCallback {
    fun onSuccess(user: MUserModel?)
    fun onError(error: String?)
}
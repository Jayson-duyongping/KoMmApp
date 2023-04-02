package com.jayson.komm.mvvm.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.jayson.komm.mvvm.BR

/**
 * @Author: Jayson
 * @CreateDate: 2023/3/5 20:55
 * @Version: 1.0
 * @Description:
 */
data class User(val name: String, val age: Int)

class UserOne : BaseObservable() {
    @get:Bindable
    var firstName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.firstName)
        }

    @get:Bindable
    var lastName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.lastName)
        }
}

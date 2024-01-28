package com.jayson.komm.api.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

open class ListConverter<T>(cls: Class<T>) {
    private val gson = Gson()
    private val cls = cls

    @TypeConverter
    fun fromList(list: List<T>?): String {
        return gson.toJson(list)
    }

    /**
     * 解决om.google.gson.internal.LinkedTreeMap cannot be cast to问题
     */
    @TypeConverter
    fun toList(string: String): List<T>? {
        return kotlin.runCatching {
            val type = TypeToken.getParameterized(MutableList::class.java, cls).type
            gson.fromJson<ArrayList<T>>(string, type)
        }.getOrElse {
            null
        }
    }
}
package com.jayson.komm.api.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jayson.komm.api.bean.Film
import com.jayson.komm.api.db.dao.FilmDao
import com.jayson.komm.common.util.LogUtils

private const val DATABASE_VERSION = 1
private const val DATABASE_NAME = "koMmApi.db"
private var needInit = true

@Database(
    entities = [Film::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class ApiDatabase : RoomDatabase() {

    abstract fun filmDao(): FilmDao

    companion object {
        const val TAG = "ApiDatabase"
        private lateinit var application: Context

        val db: ApiDatabase by lazy {
            Room.databaseBuilder(application, ApiDatabase::class.java, DATABASE_NAME)
                .addCallback(CreatedCallBack)
                .build()
        }

        fun initDB(context: Context) {
            if (needInit) {
                application = context.applicationContext
                LogUtils.d(TAG, "init  database ")
            } else {
                LogUtils.d(TAG, "initDB:The database has been initialized")
            }
        }

        private object CreatedCallBack : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                //在新装app时会调用，调用时机为数据库build()之后，数据库升级时不调用此函数
                LogUtils.d(TAG, "CreatedCallBack")
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                // 数据库打开时回调,表示初始化完成
                LogUtils.d(TAG, "CreatedCallBack 数据库打开 ")
                needInit = false
            }
        }
    }
}
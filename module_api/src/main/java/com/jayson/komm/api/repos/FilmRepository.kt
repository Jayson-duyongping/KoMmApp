package com.jayson.komm.api.repos

import com.jayson.komm.api.bean.Film
import com.jayson.komm.api.db.ApiDatabase
import com.jayson.komm.api.net.FilmHttpService
import com.jayson.komm.common.util.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

object FilmRepository {

    private const val TAG = "FilmRepository"

    suspend fun getLocalFilmsByName(keywords: String?): Flow<List<Film>?> = flow {
        LogUtils.d(TAG, "getLocalFilmsByName, keywords: $keywords")
        ApiDatabase.db.filmDao().getFilmsByName(keywords ?: "").let {
            LogUtils.d(TAG, "getLocalFilmsByName, localFilms: $it")
            emit(it)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getRemoteFilmsByNameAndInsertLocal(keywords: String?): Flow<List<Film>?> = flow {
        LogUtils.d(TAG, "getRemoteFilmsByNameAndInsertLocal, keywords: $keywords")
        val remoteFilms = FilmHttpService.getSearchFilmListByKeywords(keywords ?: "")?.also {
            LogUtils.d(TAG, "getRemoteFilmsByNameAndInsertLocal, remoteFilms: $it")
            ApiDatabase.db.filmDao().insertList(it)
        }
        emit(remoteFilms)
    }.flowOn(Dispatchers.IO)

    suspend fun getLocalFilmByDoubanId(doubanId: String?): Flow<Film?> = flow {
        LogUtils.d(TAG, "getLocalFilmsById, doubanId: $doubanId")
        val localFilm = ApiDatabase.db.filmDao().getFilmByDoubanId(doubanId ?: "")
        LogUtils.d(TAG, "getLocalFilmsById, localFilm: $localFilm")
        emit(localFilm)
    }.flowOn(Dispatchers.IO)

    suspend fun getRemoteFilmByDoubanIdAndInsertLocal(doubanId: String?): Flow<Film?> = flow {
        LogUtils.d(TAG, "getRemoteFilmByIdAndInsertLocal, doubanId: $doubanId")
        val remoteFilm = FilmHttpService.getFilmInfoByDoubanId(doubanId ?: "")?.also {
            LogUtils.d(TAG, "getRemoteFilmByIdAndInsertLocal, remoteFilm: $it")
            ApiDatabase.db.filmDao().insert(it)
        }
        emit(remoteFilm)
    }.flowOn(Dispatchers.IO)
}
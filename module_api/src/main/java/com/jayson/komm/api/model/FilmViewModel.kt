package com.jayson.komm.api.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jayson.komm.api.bean.Film
import com.jayson.komm.api.repos.FilmRepository
import com.jayson.komm.common.util.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FilmViewModel : ViewModel() {

    companion object {
        private const val TAG = "FilmViewModel"
    }

    val filmLiveData = MutableLiveData<Film?>()
    val filmsLiveData = MutableLiveData<List<Film>?>()

    private suspend fun updateLiveData(block: () -> Unit) {
        withContext(Dispatchers.Main) {
            block()
        }
    }

    fun searchFilm(keywords: String?) {
        LogUtils.d(TAG, "searchFilm, keywords: $keywords")
        viewModelScope.launch {
            // 从本地取
            FilmRepository.getLocalFilmsByName(keywords).collect { locals ->
                if (locals?.isNotEmpty() == true) {
                    updateLiveData { filmsLiveData.value = locals }
                }
                if (locals?.isEmpty() == true) {
                    // 从服务端取并更新到本地数据库
                    FilmRepository.getRemoteFilmsByNameAndInsertLocal(keywords).collect { remotes ->
                        // 如果取的本地数据是空的，那么从服务端上取下来的数据再更新UI
                        updateLiveData { filmsLiveData.value = remotes }
                    }
                }
            }
        }
    }

    fun getFilmInfoById(doubanId: String) {
        LogUtils.d(TAG, "getFilmInfoById, doubanId: $doubanId")
        viewModelScope.launch {
            // 从本地取
            FilmRepository.getLocalFilmByDoubanId(doubanId).collect { local ->
                // 本地有数据则更新UI
                local?.let {
                    updateLiveData { filmLiveData.value = it }
                }
                // 获取完整信息时，不仅要考虑数据库对象是null还要考虑其中的详细特例director有没有
                if ((local == null) || local.director.isNullOrEmpty()) {
                    // 从服务端取并更新到本地数据库
                    FilmRepository.getRemoteFilmByDoubanIdAndInsertLocal(doubanId).collect { remote ->
                        // 如果取的本地数据是空的，那么从服务端上取下来的数据再更新UI
                        updateLiveData { filmLiveData.value = remote }
                    }
                }
            }
        }
    }
}
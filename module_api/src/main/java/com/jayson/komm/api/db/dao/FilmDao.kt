package com.jayson.komm.api.db.dao

import androidx.room.*
import com.jayson.komm.api.bean.Film
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(dataList: List<Film>): LongArray

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(film: Film): Long

    @Update
    suspend fun updateList(dataList: List<Film>)

    @Delete
    suspend fun delete(film: Film)

    @Query("DELETE FROM film")
    suspend fun deleteAll()

    @Query("SELECT * FROM film")
    suspend fun getAll(): List<Film>

    @Query("SELECT * FROM film WHERE data LIKE '%' || :keyword || '%'")
    suspend fun getFilmsByName(keyword: String): List<Film>

    @Query("SELECT * FROM film WHERE doubanId = :doubanId")
    suspend fun getFilmByDoubanId(doubanId: String): Film?

    @Query("SELECT * FROM film WHERE doubanId = :doubanId")
    fun getFilmsByDoubanIdObserve(doubanId: String): Flow<Film>
}
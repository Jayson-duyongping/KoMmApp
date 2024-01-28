package com.jayson.komm.api.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.jayson.komm.api.db.converter.*

@Entity(tableName = "film")
@TypeConverters(
    FilmWithDataListConverter::class,
    FilmWithDirectorListConverter::class,
    FilmWithWriterListConverter::class,
    FilmWithActorListConverter::class,
    FilmWithDataXListConverter::class
)
data class Film(
    @PrimaryKey val doubanId: String = "",
    val doubanRating: String?,
    val doubanVotes: Int?,
    val duration: Int?,
    val id: String?,
    val imdbId: String?,
    val imdbRating: String?,
    val imdbVotes: Int?,
    val originalName: String?,
    val rottenRating: String?,
    val rottenVotes: Int?,
    val type: String?,
    val updatedAt: Long?,
    val year: String?,
    val alias: String?,
    val createdAt: Long?,
    val dateReleased: String?,
    val `data`: List<Data>?,
    val director: List<Director>?,
    val writer: List<Writer>?,
    val actor: List<Actor>?,
)

data class Data(
    val id: String?,
    val country: String?,
    val createdAt: Long?,
    val description: String?,
    val genre: String?,
    val lang: String?,
    val language: String?,
    val movie: String?,
    val name: String?,
    val poster: String?,
    val shareImage: String?,
    val updatedAt: Long?
)

data class Actor(
    val id: String?,
    val createdAt: Long?,
    val `data`: List<DataX>?,
    val updatedAt: Long?
)

data class Director(
    val id: String?,
    val createdAt: Long?,
    val `data`: List<DataX>?,
    val updatedAt: Long?
)

data class Writer(
    val id: String?,
    val createdAt: Long?,
    val `data`: List<DataX>?,
    val updatedAt: Long?
)

data class DataX(
    val id: String?,
    val createdAt: Long?,
    val lang: String?,
    val name: String?,
    val person: String?,
    val updatedAt: Long?
)
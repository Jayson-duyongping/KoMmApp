package com.jayson.komm.api.db.converter

import com.jayson.komm.api.bean.*

class FilmWithActorListConverter : ListConverter<Actor>(Actor::class.java)

class FilmWithDataListConverter : ListConverter<Data>(Data::class.java)

class FilmWithDataXListConverter : ListConverter<DataX>(DataX::class.java)

class FilmWithDirectorListConverter : ListConverter<Director>(Director::class.java)

class FilmWithWriterListConverter : ListConverter<Writer>(Writer::class.java)
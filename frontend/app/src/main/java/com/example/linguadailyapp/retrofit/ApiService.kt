package com.example.linguadailyapp.retrofit

import com.example.linguadailyapp.datamodels.AvailableWord
import com.example.linguadailyapp.datamodels.Language
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("words")
    suspend fun getWordsWithSkipAndLimit(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int,
        @Query("language") language: String
    ): List<AvailableWord>

    @GET("words")
    suspend fun getAllWords() : List<AvailableWord>

}
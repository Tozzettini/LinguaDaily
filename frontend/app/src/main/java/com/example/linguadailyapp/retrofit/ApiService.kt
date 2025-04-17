package com.example.linguadailyapp.retrofit

import com.example.linguadailyapp.database.availableword.AvailableWord
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("words")
    suspend fun getWordsWithSkipAndLimit(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int
    ): List<AvailableWord>

    @GET("words")
    suspend fun getAllWords() : List<AvailableWord>

}
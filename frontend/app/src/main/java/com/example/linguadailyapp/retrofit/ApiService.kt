package com.example.linguadailyapp.retrofit

import com.example.linguadailyapp.database.word.Word
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("words")
    suspend fun getWordsSince(@Query("since") since: String): List<Word>

    @GET("words")
    suspend fun getAllWords(): List<Word>
}
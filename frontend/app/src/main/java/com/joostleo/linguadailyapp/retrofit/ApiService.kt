package com.joostleo.linguadailyapp.retrofit

import com.joostleo.linguadailyapp.datamodels.AvailableWord
import com.joostleo.linguadailyapp.datamodels.Feedback
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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

    @POST("feedback")
    suspend fun saveFeedback(@Body feedback: Feedback)

}
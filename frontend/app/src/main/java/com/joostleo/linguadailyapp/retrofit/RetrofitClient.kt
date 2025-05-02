package com.joostleo.linguadailyapp.retrofit

import com.joostleo.linguadailyapp.datamodels.Language
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val gson: Gson = GsonBuilder()
    .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
    .registerTypeAdapter(Language::class.java, LanguageAdapter())
    .create()

object RetrofitClient {
    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://77.172.234.33:8181")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}

class LocalDateAdapter : JsonDeserializer<LocalDate>, JsonSerializer<LocalDate> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDate {
        return LocalDate.parse(json.asJsonPrimitive.asString, formatter)
    }

    override fun serialize(src: LocalDate, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.format(formatter))
    }
}

class LanguageAdapter : JsonDeserializer<Language>, JsonSerializer<Language> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Language {
        return Language.entries.firstOrNull {
            it.name.equals(json.asString, ignoreCase = true)
        } ?: throw JsonParseException("Unknown language: ${json.asString}")
    }

    override fun serialize(src: Language, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.name)
    }
}


package com.joostleo.linguadailyapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.joostleo.linguadailyapp.datamodels.AvailableWord
import com.joostleo.linguadailyapp.database.availableword.AvailableWordDao
import com.joostleo.linguadailyapp.datamodels.LearnedWord
import com.joostleo.linguadailyapp.database.learnedWord.LearnedWordDao
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import com.joostleo.linguadailyapp.datamodels.Language

class Converters {
    private val localDateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.format(localDateFormatter)
    }

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it, localDateFormatter) }
    }


    private val instantFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @TypeConverter
    fun fromInstant(instant: Instant?): String? {
        return instant?.atZone(ZoneOffset.UTC)?.format(instantFormatter)
    }

    @TypeConverter
    fun toInstant(value: String?): Instant? {
        return value?.let {
            LocalDateTime.parse(it, instantFormatter)
                .atZone(ZoneOffset.UTC)
                .toInstant()
        }
    }


    private val localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? {
        return dateTime?.format(localDateTimeFormatter)
    }

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let {
            LocalDateTime.parse(it, localDateTimeFormatter)
        }
    }

    @TypeConverter
    fun fromLanguage(value: Language): String = value.name

    @TypeConverter
    fun toLanguage(value: String): Language {
        return Language.entries.firstOrNull {
            it.displayName.equals(value, ignoreCase = true) || it.name.equals(value, ignoreCase = true)
        } ?: Language.ENGLISH
    }

}

@Database(entities = [LearnedWord::class, AvailableWord::class], version = 7, exportSchema = false)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun learnedWordDao(): LearnedWordDao
    abstract fun availableWordDao(): AvailableWordDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getInstance(context: Context): LocalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    "word_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

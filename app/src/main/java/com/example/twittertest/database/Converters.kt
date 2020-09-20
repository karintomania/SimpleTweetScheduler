package com.example.twittertest.database

import android.annotation.SuppressLint
import android.util.Log
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.util.Date


class Converters {
    @TypeConverter
    fun toDate(dateString:String? ): LocalDateTime? {
        dateString?.let{
            return LocalDateTime.parse(it)
        }
        return null
    }

    @TypeConverter
    fun toDateString(date: LocalDateTime?): String?{
        date?.let{
            return it.toString()
        }
        return null
    }
}

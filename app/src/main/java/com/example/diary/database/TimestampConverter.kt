package com.example.diary.database

import androidx.room.TypeConverter
import java.time.Instant
import java.time.ZonedDateTime
import java.util.*

class DateTimeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long): ZonedDateTime{
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(value), TimeZone.getDefault().toZoneId()) }

    @TypeConverter
    fun dateToTimestamp(date: ZonedDateTime): Long {
        return date.toEpochSecond()*1000
    }

}
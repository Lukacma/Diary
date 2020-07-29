package com.example.diary.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime


@Entity
data class DiaryNotes(
    @PrimaryKey(autoGenerate = true) val id:Long=0,
    @ColumnInfo(name="time_taken") val time: ZonedDateTime,
    @ColumnInfo(name="note") val note:String)

package com.example.diary.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.time.ZonedDateTime

@Dao
interface DiaryDao {
    @Query("Select * from diarynotes where time_taken BETWEEN :dayst AND :dayet")
    fun getNotes(dayst: ZonedDateTime, dayet: ZonedDateTime): LiveData<List<DiaryNotes>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNote(note: DiaryNotes): Long

    @Query("Select time_taken from diarynotes order by time_taken asc Limit 1")
    fun getEarliestNote(): LiveData<ZonedDateTime>

}
package com.example.diary.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.time.ZonedDateTime

@Dao
interface DiaryDao {
    @Transaction @Query("Select * from diarynotes where time_taken BETWEEN :dayst AND :dayet")
    fun getNotes(dayst: ZonedDateTime, dayet:ZonedDateTime):LiveData<List<DiaryNotes>>

    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insertNote(note:DiaryNotes):Long


}
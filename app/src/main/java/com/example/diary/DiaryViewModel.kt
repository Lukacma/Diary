package com.example.diary

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.diary.database.DiaryDao
import com.example.diary.database.DiaryNotes
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime


val midnight: LocalTime = LocalTime.MIDNIGHT
class DiaryViewModel(private val dao:DiaryDao):ViewModel() {

    private val job = Job()
    private val viewModelScope = CoroutineScope(job + Dispatchers.Main)
    private val currentDay: MutableLiveData<LocalDate> = MutableLiveData(LocalDate.now())
    val firstNoteDate: LiveData<LocalDate> =
        Transformations.map(dao.getEarliestNote()) { zoneDate -> zoneDate.toLocalDate() }
    val notes = Transformations.switchMap(currentDay) { date ->
        loadNotes(date)
    }

    val listVisible = Transformations.map(notes) {
        it?.let {
            if (it.isEmpty()) View.VISIBLE
            else View.GONE
        } ?: View.VISIBLE
    }

    fun loadNotes(date: LocalDate): LiveData<List<DiaryNotes>> {
        val dayst = date.atStartOfDay(ZoneId.systemDefault())
        val dayet = date.plusDays(1).atStartOfDay(ZoneId.systemDefault())
        return dao.getNotes(dayst, dayet)
    }

    fun insertNote(note: String) {
        viewModelScope.launch {
            val time = ZonedDateTime.now()
            withContext(Dispatchers.IO) {
                val memento = DiaryNotes(time = time, note = note)
                dao.insertNote(memento)
            }
        }
    }

    fun setNewDate(date: LocalDate) {
        currentDay.value = date
    }


    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}
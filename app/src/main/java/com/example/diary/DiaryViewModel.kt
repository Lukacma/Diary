package com.example.diary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.diary.database.DiaryDao
import com.example.diary.database.DiaryNotes
import kotlinx.coroutines.*
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.*


val midnight: LocalTime = LocalTime.MIDNIGHT
class DiaryViewModel(private val dao:DiaryDao):ViewModel() {

    private val job= Job()
    private val viewModelScope= CoroutineScope(job+Dispatchers.Main)
    private val currentDay:MutableLiveData<ZonedDateTime> =MutableLiveData(ZonedDateTime.now())
    val notes=Transformations.switchMap(currentDay) { date ->
        loadNotes(date)
    }

    val listVisible=Transformations.map(notes){
        it?.isNotEmpty()
    }

    fun loadNotes(date:ZonedDateTime):LiveData<List<DiaryNotes>>{
            val dayst=date.truncatedTo(ChronoUnit.DAYS)
            val dayet=date.plusDays(1)
            return dao.getNotes(dayst, dayet)
    }

    fun insertNote(note:String) {
        viewModelScope.launch {
            val time = ZonedDateTime.now(TimeZone.getDefault().toZoneId())
            withContext(Dispatchers.IO) {
                val memento = DiaryNotes(time = time, note = note)
                val isn = dao.insertNote(memento)
            }
        }
    }
    fun setNewDate(date:ZonedDateTime){
        currentDay.value=date;
    }




    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}
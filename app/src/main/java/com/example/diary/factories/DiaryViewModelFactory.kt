package com.example.diary.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.diary.DiaryViewModel
import com.example.diary.database.DiaryDao

class DiaryViewModelFactory(private val dao:DiaryDao):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DiaryViewModel::class.java)){
            return DiaryViewModel(dao) as T
        }
        throw IllegalArgumentException("Illegal class")
    }
}
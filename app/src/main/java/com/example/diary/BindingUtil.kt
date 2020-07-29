package com.example.diary

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@BindingAdapter("showTime")
fun showTime(text:TextView,date: ZonedDateTime){
    text.text=date.format(DateTimeFormatter.ofPattern("HH:mm"))
}
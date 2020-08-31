package com.example.diary.Calendar

import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.diary.R
import com.example.diary.databinding.CalendarDayLayoutBinding
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.ViewContainer
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DayViewContainer(view: View, clickFunction: (d: CalendarDay) -> Unit) : ViewContainer(view) {
    companion object Formatters {
        val dateFormatter = DateTimeFormatter.ofPattern("dd")
        val dayFormatter = DateTimeFormatter.ofPattern("EEE")
        val monthFormatter = DateTimeFormatter.ofPattern("MMM")
    }

    val bind = CalendarDayLayoutBinding.bind(view)
    lateinit var day: CalendarDay

    init {
        view.setOnClickListener {
            clickFunction(day)
        }

        // Example: If you want the clicked date to always be centered on the screen,
        // you would use: exSevenCalendar.smoothScrollToDate(day.date.minusDays(2))


    }


    fun bind(day: CalendarDay, selectedDate: LiveData<LocalDate>) {
        this.day = day
        bind.DateText.text = dateFormatter.format(day.date)
        bind.DayText.text = dayFormatter.format(day.date)
        bind.MonthText.text = monthFormatter.format(day.date)
        selectedDate.observe(view.context as LifecycleOwner, Observer {
            bind.DateText.setTextColor(view.context.getColor(if (day.date == it) R.color.colorAccent else R.color.white))
            bind.SelectedView.isVisible = day.date == it
        })
    }
}



package com.example.diary

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diary.Calendar.DayViewContainer
import com.example.diary.database.DiaryDatabase
import com.example.diary.databinding.CalendarViewBinding
import com.example.diary.factories.DiaryViewModelFactory
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.utils.Size
import com.kizitonwose.calendarview.utils.yearMonth
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*


@Suppress("DEPRECATION")
class CalendarFragment: Fragment() {
    private lateinit var viewModel: DiaryViewModel
    private lateinit var binding: CalendarViewBinding
    private val adapter = RecyclerViewAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val diaryDao = DiaryDatabase.getDatabase(this.requireContext()).dao()
        val ViewModelFactory = DiaryViewModelFactory(diaryDao)
        viewModel = ViewModelProviders.of(this, ViewModelFactory).get(DiaryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.calendar_view, container, false)
        binding.lifecycleOwner = this
        val arg = CalendarFragmentArgs.fromBundle(requireArguments()).note
        arg?.let {
            viewModel.insertNote(arg)
        }
        binding.viewModel = viewModel
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        calendarSetup()
        viewModel.firstNoteDate.observe(viewLifecycleOwner, Observer {
            binding.calendarView.updateMonthRange(it.yearMonth, YearMonth.now())
            binding.calendarView.scrollToDate(LocalDate.now())
        })


        viewModel.notes.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        binding.createNote.setOnClickListener {
            it.findNavController()
                .navigate(CalendarFragmentDirections.actionCalendarFragmentToNoteWritingFragment())
        }
        return binding.root
    }

    private fun calendarSetup() {
        val dm = DisplayMetrics()
        val wm = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(dm)
        binding.calendarView.apply {
            val dayWidth = dm.widthPixels / 5
            val dayHeight = (dayWidth * 1.05).toInt()
            daySize = Size(dayWidth, dayHeight)
        }
        val firstMonth = YearMonth.now()
        val currentMonth = YearMonth.now()
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view) { day ->
                val firstDay = binding.calendarView.findFirstVisibleDay()
                val lastDay = binding.calendarView.findLastVisibleDay()
                if (firstDay == day) {
                    // If the first date on screen was clicked, we scroll to the date to ensure
                    // it is fully visible if it was partially off the screen when clicked.
                    binding.calendarView.smoothScrollToDate(day.date)
                } else if (lastDay == day) {
                    // If the last date was clicked, we scroll to 4 days ago, this forces the
                    // clicked date to be fully visible if it was partially off the screen.
                    // We scroll to 4 days ago because we show max of five days on the screen
                    // so scrolling to 4 days ago brings the clicked date into full visibility
                    // at the end of the calendar view.
                    binding.calendarView.smoothScrollToDate(day.date.minusDays(4))
                }
                if (viewModel.currentDate.value != day.date) {
                    viewModel.setNewDate(day.date)
                    binding.calendarView.notifyDateChanged(day.date)
                }
            }

            override fun bind(container: DayViewContainer, day: CalendarDay) =
                container.bind(day, viewModel.currentDate)
        }

        binding.calendarView.setup(firstMonth, currentMonth, firstDayOfWeek)
        binding.calendarView.scrollToDate(LocalDate.now())
    }

}
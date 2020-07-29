package com.example.diary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diary.database.DiaryDatabase
import com.example.diary.databinding.CalendarViewBinding
import com.example.diary.factories.DiaryViewModelFactory
import java.time.LocalDate
import java.util.*


@Suppress("DEPRECATION")
class CalendarFragment: Fragment() {
    private lateinit var viewModel:DiaryViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val diaryDao=DiaryDatabase.getDatabase(this.requireContext()).dao()
        val ViewModelFactory= DiaryViewModelFactory(diaryDao)
        viewModel=ViewModelProviders.of(this,ViewModelFactory).get(DiaryViewModel::class.java)
        val binding:CalendarViewBinding=DataBindingUtil.inflate(inflater,R.layout.calendar_view,container,false)
        binding.lifecycleOwner = this
        val arg=CalendarFragmentArgs.fromBundle(requireArguments()).note
        arg?.let {
            viewModel.insertNote(arg)
        }
        val adapter=RecyclerViewAdapter()
        binding.recyclerView.adapter=adapter
        binding.recyclerView.layoutManager=LinearLayoutManager(activity)



        binding.calendarView.setOnDateChangeListener { _, year, month, day ->
            binding.calendarView.date=LocalDate.of(year,month+1,day).atStartOfDay(TimeZone.getDefault().toZoneId()).toEpochSecond()*1000
            val currentDate=LocalDate.of(year,month+1,day).atStartOfDay(TimeZone.getDefault().toZoneId())
            viewModel.setNewDate(currentDate)
        }

        viewModel.notes.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapter.submitList(it)
        })

        binding.createNote.setOnClickListener {
            it.findNavController().navigate(CalendarFragmentDirections.actionCalendarFragmentToNoteWritingFragment())
        }

        return binding.root
    }


}
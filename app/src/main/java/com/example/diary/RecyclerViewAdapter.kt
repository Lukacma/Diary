package com.example.diary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.diary.database.DiaryNotes
import com.example.diary.databinding.NoteLayoutBinding

class RecyclerViewAdapter: ListAdapter<DiaryNotes,NoteViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(NoteLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.onBindData(getItem(position))
    }

    companion object DiffCallback:DiffUtil.ItemCallback<DiaryNotes>(){
        override fun areItemsTheSame(oldItem: DiaryNotes, newItem: DiaryNotes): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DiaryNotes, newItem: DiaryNotes): Boolean {
            return (oldItem.note==newItem.note)&&(oldItem.time==newItem.time)
        }
    }
}


class NoteViewHolder(private var binding: NoteLayoutBinding):RecyclerView.ViewHolder(binding.root){


    fun onBindData(note:DiaryNotes) {
        binding.diaryNote = note
        binding.executePendingBindings()
        binding.noteText.post(Runnable {
            val layout = binding.noteText.layout
            if (layout.getEllipsisCount(3) > 0) {
                binding.root.setOnClickListener {
                    it.findNavController().navigate(
                        CalendarFragmentDirections.actionCalendarFragmentToNoteViewFragment(note.note)
                    )
                }
            } else {
                binding.root.setOnClickListener(null)
            }
        })

    }

}


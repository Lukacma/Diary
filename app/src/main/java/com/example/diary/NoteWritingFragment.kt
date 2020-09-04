package com.example.diary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.diary.databinding.NoteWritingLayoutBinding

class NoteWritingFragment:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = NoteWritingLayoutBinding.inflate(inflater)

        binding.saveButton.setOnClickListener {
            val note = binding.editTextTextPersonName.text.toString()
            it.findNavController().navigate(
                NoteWritingFragmentDirections.actionNoteWritingFragmentToCalendarFragment(note)
            )
        }
        binding.cancelButton.setOnClickListener {
            it.findNavController().navigate(
                NoteWritingFragmentDirections.actionNoteWritingFragmentToCalendarFragment(null)
            )
        }
        return binding.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(NoteWritingFragmentDirections.actionNoteWritingFragmentToCalendarFragment())
        }
    }
}
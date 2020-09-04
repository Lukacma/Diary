package com.example.diary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.diary.databinding.NoteViewLayoutBinding

class NoteViewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = NoteViewLayoutBinding.inflate(inflater, container, false)
        binding.textView.text = NoteViewFragmentArgs.fromBundle(requireArguments()).noteToExpand
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(NoteViewFragmentDirections.actionNoteViewFragmentToCalendarFragment())
        }
    }
}
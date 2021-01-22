package com.example.notes

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.notes.database.NotesDatabase
import com.example.notes.entities.Notes
import com.example.notes.util.NoteBottomSheetFragment
import kotlinx.android.synthetic.main.fragment_create_note.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CreateNoteFragment : Basefragment() {
    private var currentDate: String? = null
    var selectedColor = "#171C26"
    private var noteId = -1

//fungsi untuk membuat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteId = requireArguments().getInt("noteId",-1)
    }
//fungsi untuk view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_note, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            CreateNoteFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (noteId != -1) {
            launch {
                context?.let {
                    var notes = NotesDatabase.getDatabase(it).noteDao().getSpecificNote(noteId)
                    etNotesTitle.setText(notes.title)
                    etNotesSubtitle.setText(notes.subTitle)
                    etNotesDesc.setText(notes.noteText)
                    colorView.setBackgroundColor(Color.parseColor(notes.color))
                }
            }
        }

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            BroadcastReceiver, IntentFilter("bottom_sheet_action")
        )
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        currentDate = sdf.format(Date())

        colorView.setBackgroundColor(Color.parseColor(selectedColor))


        tvDateTime.text = currentDate

        imgSave.setOnClickListener{
//save dan update notes

            if(noteId != -1)
            {
                updateNote()
            }else{
                saveNote()
            }

        }
        imgBack.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }
        imgMore.setOnClickListener{
            var noteBottomSheetFragment = NoteBottomSheetFragment.newInstance(noteId)
            noteBottomSheetFragment.show(requireActivity().supportFragmentManager,"Note Bottom Sheet Fragment")

        }

    }
//mengupdate note
    private  fun updateNote(){
        launch {
            if (etNotesTitle.text.isNullOrEmpty()){
                Toast.makeText(context,"Judul harus diisi",Toast.LENGTH_SHORT).show()
            }
            else if (etNotesSubtitle.text.isNullOrEmpty()){
                Toast.makeText(context,"sub judul harus diisi",Toast.LENGTH_SHORT).show()
            }
            else if (etNotesDesc.text.isNullOrEmpty()){
                Toast.makeText(context,"isi Deskripsi",Toast.LENGTH_SHORT).show()
            }else{
            context?.let {
                var notes = NotesDatabase.getDatabase(it).noteDao().getSpecificNote(noteId)
                notes.title = etNotesTitle.text.toString()
                notes.subTitle = etNotesSubtitle.text.toString()
                notes.noteText = etNotesDesc.text.toString()
                notes.dateTime = currentDate
                notes.color = selectedColor

                NotesDatabase.getDatabase(it).noteDao().updateNotes(notes)
                etNotesTitle.setText("")
                etNotesSubtitle.setText("")
                etNotesDesc.setText("")
                Toast.makeText(context,"Notes kamu sudah terupdate",Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.popBackStack()
                }
            }
        }
    }
//menghapus note
    private fun deleteNote(){
        launch {
            context?.let {
                NotesDatabase.getDatabase(it).noteDao().deleteSpecificNote(noteId)
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

    }
//menyimpan note
    private fun saveNote(){
        if (etNotesTitle.text.isNullOrEmpty()){
            Toast.makeText(context,"Judul harus diisi",Toast.LENGTH_SHORT).show()
        }
        else if (etNotesSubtitle.text.isNullOrEmpty()){
            Toast.makeText(context,"sub judul harus diisi",Toast.LENGTH_SHORT).show()
        }
        else if (etNotesDesc.text.isNullOrEmpty()){
            Toast.makeText(context,"isi Deskripsi",Toast.LENGTH_SHORT).show()
        }
        else {
            launch {
                val notes = Notes()
                notes.title = etNotesTitle.text.toString()
                notes.subTitle = etNotesSubtitle.text.toString()
                notes.noteText = etNotesDesc.text.toString()
                notes.dateTime = currentDate
                notes.color = selectedColor

                context?.let {
                    NotesDatabase.getDatabase(it).noteDao().insertNotes(notes)
                    etNotesTitle.setText("")
                    etNotesSubtitle.setText("")
                    etNotesDesc.setText("")

                }
                Toast.makeText(context,"Notes kamu sudah tersimpan",Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

    }

    fun replaceFragment(fragment: Fragment, istransition:Boolean){
        val fragmentTransition = activity!!.supportFragmentManager.beginTransaction()

        if(istransition){
            fragmentTransition.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left)
        }
        fragmentTransition.replace(R.id.frame_layout,fragment).addToBackStack(fragment.javaClass.simpleName).commit()
    }

    private val BroadcastReceiver : BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {

            var actionColor =  p1!!.getStringExtra("actionColor")

            when (actionColor!!){
                "Blue" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))


                }
                "Yellow" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))


                }
                "Purple" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))


                }
                "Green" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))


                }
                "Orange" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))


                }
                "Black" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))


                }
                "DeleteNote" -> {
                    //delete note
                    deleteNote()
                }


                else  -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))


                }
            }

        }

    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(BroadcastReceiver)
        super.onDestroy()
    }
}
package com.example.notes.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.R
import com.example.notes.entities.Notes
import kotlinx.android.synthetic.main.item_rv_notes.view.*

class NotesAdapter() :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {
    var listener: OnItemClickListener? = null
    var arrList = ArrayList<Notes>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_notes,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return arrList.size
    }

    fun setData (arrNotesList: List<Notes>){
        arrList = arrNotesList as ArrayList<Notes>

    }

    fun setOnClickListener(Listener1:OnItemClickListener){
        listener = Listener1
    }
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.itemView.tvTitle.text = arrList[position].title
        holder.itemView.tvDesc.text = arrList[position].noteText
        holder.itemView.tvDateTime.text = arrList[position].dateTime

        if(arrList[position].color != null){
            holder.itemView.cardsView.setCardBackgroundColor(Color.parseColor(arrList[position].color))
        }else{

            holder.itemView.cardsView.setCardBackgroundColor(Color.parseColor(R.color.colorLightBlack.toString()))
        }
        holder.itemView.cardsView.setOnClickListener {
            listener!!.onClicked(arrList[position].id!!)
        }

    }
    class NoteViewHolder(view:View): RecyclerView.ViewHolder(view){

    }

    interface OnItemClickListener {
        fun onClicked(noteId:Int)
        
    }
}
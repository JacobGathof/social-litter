package edu.rose_hulman.tee.social_litter

import android.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import kotlinx.android.synthetic.main.group_row.view.*

class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    init{
        itemView.setOnClickListener{

        }
    }

    fun bind(group: String) {
        itemView.group_name_text.setText(group)
    }
}
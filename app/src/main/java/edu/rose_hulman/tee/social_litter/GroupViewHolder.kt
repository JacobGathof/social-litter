package edu.rose_hulman.tee.social_litter

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.group_row.view.*

class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(group: Group) {
        itemView.group_name_text.setText(group.groupName)
    }
}
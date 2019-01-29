package edu.rose_hulman.tee.social_litter

import android.content.Context
import android.provider.ContactsContract
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup

class MyGroupAdapter(var context: Context) : RecyclerView.Adapter<GroupViewHolder>() {

    var groups = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): GroupViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.group_row, parent, false)
        return GroupViewHolder(view)
    }

    override fun getItemCount(): Int {
        return groups.size
    }

    override fun onBindViewHolder(viewHolder:  GroupViewHolder, pos: Int) {
        viewHolder.bind(groups[pos])
    }

    fun addSnapshotListener() {
        Database.addMyGroupListener(this)
    }

    fun add(group: String) {
        groups.add(group)
    }

    fun addAll(gr : List<String>){
        groups.addAll(gr)
        notifyDataSetChanged()
    }
}
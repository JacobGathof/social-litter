package edu.rose_hulman.tee.social_litter

import android.content.Context
import android.provider.ContactsContract
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup

class MyGroupAdapter(var context: Context, var fragment: GroupFragment) : RecyclerView.Adapter<GroupViewHolder>(), GroupAdapter {

    var groups = ArrayList<String>()
    public var isActive = false

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): GroupViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.group_row, parent, false)
        return GroupViewHolder(view, this)
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
        notifyDataSetChanged()
    }

    fun addAll(gr : List<String>){
        groups.addAll(gr)
        notifyDataSetChanged()
    }

    override fun showGroup(pos: Int) {
        fragment.showGroup(groups[pos])
    }

    override fun removeGroup(pos: Int) {
        if (isActive) {
            Database.leaveGroup(groups[pos])
            fragment.refresh()
        }
    }
}
package edu.rose_hulman.tee.social_litter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup

class NewGroupAdapter(var context: Context, var fragment: GroupFragment): RecyclerView.Adapter<GroupViewHolder>(), GroupAdapter {

    var groups = ArrayList<String>()

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
        Database.addNewGroupListener(this)
    }

    fun add(group: String) {
        groups.add(group)
        notifyDataSetChanged()
    }

    fun addAll(gr : List<String>){
        groups.clear()
        groups.addAll(gr)
        notifyDataSetChanged()
    }

    override fun showGroup(pos: Int) {
        fragment.showGroup(groups[pos])
    }

    override fun removeGroup(pos: Int) {
        fragment.refresh()
    }
}
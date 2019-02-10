package edu.rose_hulman.tee.social_litter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log

class DragAdapter(var adapter: GroupAdapter) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
    override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSwiped(view: RecyclerView.ViewHolder, pos: Int) {
        adapter.removeGroup(view.adapterPosition)
    }
}
package edu.rose_hulman.tee.social_litter

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_groups.view.*

class GroupFragment : Fragment(){

    lateinit var adapterMy : MyGroupAdapter
    lateinit var adapterNew : NewGroupAdapter
    var listener : GroupClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_groups, container, false)
        val recycler = rootView.recycler

        adapterMy = MyGroupAdapter(this.context!!, this)
        adapterMy.addSnapshotListener()
        adapterNew = NewGroupAdapter(this.context!!, this)
        adapterNew.addSnapshotListener()

        var manager = LinearLayoutManager(this.context!!)

        recycler.layoutManager = manager
        recycler.setHasFixedSize(true)
        recycler.adapter = adapterNew

        rootView.join_toggle.setOnClickListener {
            recycler.adapter = adapterNew
            rootView.join_toggle.setBackgroundColor(context!!.resources.getColor(R.color.blue_5))
            rootView.my_groups.setBackgroundColor(context!!.resources.getColor(R.color.blue_3))
            rootView.add_group.visibility = View.VISIBLE
        }

        rootView.my_groups.setOnClickListener {
            recycler.adapter = adapterMy
            rootView.join_toggle.setBackgroundColor(context!!.resources.getColor(R.color.blue_3))
            rootView.my_groups.setBackgroundColor(context!!.resources.getColor(R.color.blue_5))
            rootView.add_group.visibility = View.INVISIBLE
        }

        return rootView
    }

    /*
    public class SwipeCallback(var adapt: MyGroupAdapter) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
        override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
            adapt.(viewHolder.adapterPosition)
        }
    }
    */

    fun showGroup(group: String) {
        listener?.onGroupSelected(group);
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is GroupClickListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnGroupSelectedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface GroupClickListener {
        fun onGroupSelected(group: String)
    }

}
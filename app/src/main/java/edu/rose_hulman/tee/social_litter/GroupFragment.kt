package edu.rose_hulman.tee.social_litter

import android.app.AlertDialog
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
import kotlinx.android.synthetic.main.add_new_group.view.*
import kotlinx.android.synthetic.main.fragment_groups.*
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

        var drag = DragAdapter(adapterMy)

        var manager = LinearLayoutManager(this.context!!)

        recycler.layoutManager = manager
        recycler.setHasFixedSize(true)
        recycler.adapter = adapterNew
        var itemTouchHelper = ItemTouchHelper(drag)
        itemTouchHelper.attachToRecyclerView(recycler)

        rootView.join_toggle.setBackgroundColor(context!!.resources.getColor(R.color.blue_2))

        rootView.join_toggle.setOnClickListener {
            recycler.adapter = adapterNew
            rootView.join_toggle.setBackgroundColor(context!!.resources.getColor(R.color.blue_2))
            rootView.my_groups.background = context!!.resources.getDrawable(R.drawable.element_border)
            rootView.add_group.visibility = View.VISIBLE
            adapterMy.isActive = false
        }

        rootView.my_groups.setOnClickListener {
            recycler.adapter = adapterMy
            rootView.my_groups.setBackgroundColor(context!!.resources.getColor(R.color.blue_2))
            rootView.join_toggle.background = context!!.resources.getDrawable(R.drawable.element_border)
            rootView.add_group.visibility = View.INVISIBLE
            adapterMy.isActive = true
        }

        rootView.add_group.setOnClickListener {

            val builder = AlertDialog.Builder(activity)
            val view = LayoutInflater.from(activity).inflate(R.layout.add_new_group, null, false)
            builder.setView(view)
            builder.setTitle("Add a new group")


            builder.setPositiveButton(android.R.string.ok){dialog, which ->
                val name = view.add_group_name.text.toString()
                val desc = view.add_group_desc.text.toString()
                val privacy = true
                Database.addGroup(Group(name, desc, privacy))
            }

            builder.setNegativeButton(android.R.string.cancel){dialog, which ->

            }

            val b = builder.create()
            b.window?.setBackgroundDrawable(context!!.getDrawable(R.drawable.element_border))
            b.show()
        }

        return rootView
    }

    public fun refresh() {
        var onMyTab = adapterMy.isActive
        adapterMy = MyGroupAdapter(this.context!!, this)
        adapterMy.addSnapshotListener()
        adapterMy.isActive = onMyTab
        adapterNew = NewGroupAdapter(this.context!!, this)
        adapterNew.addSnapshotListener()
        if (onMyTab) {
            recycler.adapter = adapterMy
        }else {
            recycler.adapter = adapterNew
        }
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
package edu.rose_hulman.tee.social_litter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_groups.view.*

class GroupFragment : Fragment(){

    lateinit var adapterMy : MyGroupAdapter
    lateinit var adapterNew : NewGroupAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_groups, container, false) as RecyclerView

        adapterMy = MyGroupAdapter(this.context!!)

        adapterMy.addSnapshotListener()

        adapterNew = NewGroupAdapter(this.context!!)

        adapterNew.addSnapshotListener()

        var manager = LinearLayoutManager(this.context!!)

        rootView.layoutManager = manager
        rootView.setHasFixedSize(true)
        rootView.adapter = adapterNew

        rootView.join_toggle.setOnClickListener {
            rootView.adapter = adapterNew
            rootView.join_toggle.setBackgroundColor(context!!.resources.getColor(R.color.blue_7))
            rootView.my_groups.setBackgroundColor(context!!.resources.getColor(R.color.blue_3))
            rootView.add_group.visibility = View.VISIBLE
        }

        rootView.my_groups.setOnClickListener {
            rootView.adapter = adapterMy
            rootView.join_toggle.setBackgroundColor(context!!.resources.getColor(R.color.blue_3))
            rootView.my_groups.setBackgroundColor(context!!.resources.getColor(R.color.blue_7))
            rootView.add_group.visibility = View.INVISIBLE
        }

        return rootView
    }

}
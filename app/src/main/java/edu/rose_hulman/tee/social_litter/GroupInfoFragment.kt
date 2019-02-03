package edu.rose_hulman.tee.social_litter

import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_group_info.view.*
import kotlinx.android.synthetic.main.fragment_groups.view.*

class GroupInfoFragment : Fragment() {

    lateinit var group: Group

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            group = it!!.getParcelable(ARG_GROUP)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_group_info, container, false)

        rootView.group_info_title.setText(group.groupName)
        rootView.group_info_desc.setText(group.description)
        rootView.group_info_back.setOnClickListener {
            //activity!!.finish()
            val man = activity!!.supportFragmentManager
            man.popBackStack()
        }
        rootView.group_info_edit.visibility = View.INVISIBLE
        if (Database.user!!.groups.contains(group.groupName)) {
            rootView.group_info_join.visibility = View.INVISIBLE
        }
        rootView.group_info_join.setOnClickListener {
            Database.joinGroup(group.groupName)
            val man = activity!!.supportFragmentManager
            man.popBackStack()
        }
        return rootView
    }

    companion object {

        private val ARG_GROUP = "group"

        fun newInstance(group: Group): GroupInfoFragment {
            val fragment = GroupInfoFragment()
            val args = Bundle()
            args.putParcelable(ARG_GROUP, group)
            fragment.arguments = args
            return fragment
        }
    }
}
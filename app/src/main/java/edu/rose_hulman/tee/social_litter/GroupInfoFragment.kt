package edu.rose_hulman.tee.social_litter

import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_group_info.view.*
import kotlinx.android.synthetic.main.fragment_groups.view.*
import android.widget.LinearLayout



class GroupInfoFragment : Fragment() {

    lateinit var group: Group
    lateinit var rootView : View

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
        rootView = inflater.inflate(R.layout.fragment_group_info, container, false)

        rootView.group_info_title.setText(group.groupName)
        rootView.group_info_desc.setText(group.description)
        Database.getUsersInGroup(group, this)

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

    fun addMembersList(members : List<String>){
        for(s in members){
            val params = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            params.setMargins(8, 8, 8, 8)

            var b = Button(context!!)
            b.text = s
            b.setPadding(8,8,8,8)
            b.background = context!!.getDrawable(R.drawable.element_border)
            rootView.group_members_list.addView(b, params)
        }
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
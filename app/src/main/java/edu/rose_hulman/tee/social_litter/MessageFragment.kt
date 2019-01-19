package edu.rose_hulman.tee.social_litter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.fragment_messages.view.*

class MessageFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_messages, container, false)


        rootView.post_button.setOnClickListener{
            val groupName = rootView.dropdown_group.selectedItem.toString()
            val location = GeoPoint(0.0, 0.0)
            val title = rootView.input_title.text.toString()
            val body = rootView.input_message.text.toString()
            Database.addMessage(Message(groupName, "Eric", title, body, location, 1.0, 0))
        }

        var groups = Array<String>(3) {
            when (it) {
                0 -> "Group1"
                1 -> "Group2"
                else -> "Group3"
            }
        }

//        var adapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, groups)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
//
//        rootView.dropdown_group.adapter = adapter;

        return rootView
    }

    companion object {

        private val ARG_SECTION_NUMBER = "section_number"

        fun newInstance(sectionNumber: Int): MessageFragment {
            val fragment = MessageFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }

}
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

    private lateinit var locationService: LocationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            locationService = it!!.getParcelable(MessageFragment.ARG_LOCATION)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_messages, container, false)


        rootView.post_button.setOnClickListener{
            val groupName = rootView.dropdown_group.selectedItem.toString()
            val location = GeoPoint(locationService.getLocation().latitude, locationService.getLocation().longitude)
            val title = rootView.input_title.text.toString()
            val body = rootView.input_message.text.toString()
            val radius = rootView.radius.text.toString().toDouble()
            rootView.input_title.setText("")
            rootView.input_message.setText("")
            rootView.radius.setText("")
            Database.addMessage(Message(groupName, "Eric", title, body, location, radius, 0))
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

        private val ARG_LOCATION = "section_number"

        fun newInstance(locationService: LocationService): MessageFragment {
            val fragment = MessageFragment()
            val args = Bundle()
            args.putParcelable(ARG_LOCATION, locationService)
            fragment.arguments = args
            return fragment
        }
    }

}
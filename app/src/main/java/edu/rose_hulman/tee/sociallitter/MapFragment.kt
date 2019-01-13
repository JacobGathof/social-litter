package edu.rose_hulman.tee.sociallitter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_map.view.*
import kotlinx.android.synthetic.main.fragment_messages.view.*
import com.google.android.gms.maps.MapsInitializer



class MapFragment : Fragment(), OnMapReadyCallback{

    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_map, container, false)

        mapFragment = rootView.map
        mapFragment.onCreate(savedInstanceState)
        mapFragment.onResume()
        mapFragment.getMapAsync(this)

        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Exception) {
            Log.d("QQQQQ", "Initializer Failed")
            e.printStackTrace()
        }


        return rootView
    }


    override fun onMapReady(googleMap: GoogleMap) {
        Log.d("QQQQQ", "Map is ready")
        mMap = googleMap

        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    companion object {

        private val ARG_SECTION_NUMBER = "section_number"

        fun newInstance(sectionNumber: Int): MapFragment {
            val fragment = MapFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        mapFragment.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapFragment.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapFragment.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapFragment.onStop()
    }
}
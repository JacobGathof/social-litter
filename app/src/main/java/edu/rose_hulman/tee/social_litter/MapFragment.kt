package edu.rose_hulman.tee.social_litter

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.fragment_map.view.*


class MapFragment : Fragment(), OnMapReadyCallback{

    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: MapView
    private lateinit var locationService: LocationService
    var marker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            locationService = it!!.getParcelable(ARG_LOCATION)
        }
    }

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
        mMap = googleMap
        locationService.addMap(this)

        mMap.setMinZoomPreference(mMap.minZoomLevel/4.0f)

        val sydney = LatLng(-34.0, 151.0)
        marker = mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney").flat(true))

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    fun updatePosition(location: Location) {
        val loc = LatLng(location.latitude, location.longitude)
        marker?.remove()
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(loc))
        marker = mMap.addMarker(MarkerOptions().position(loc).title("Marker in Sydney").flat(true))
    }

    companion object {

        private val ARG_LOCATION = "section_number"

        fun newInstance(locationService: LocationService): MapFragment {
            val fragment = MapFragment()
            val args = Bundle()
            args.putParcelable(ARG_LOCATION, locationService)
            fragment.arguments = args
            return fragment
        }
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
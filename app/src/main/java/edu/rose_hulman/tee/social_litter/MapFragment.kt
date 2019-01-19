package edu.rose_hulman.tee.social_litter

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.Marker
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.fragment_map.view.*


class MapFragment : Fragment(), OnMapReadyCallback{

    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: MapView
    private lateinit var locationService: LocationService
    var marker: Marker? = null
    var mapController : MapController? = null

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

        setHasOptionsMenu(true)

        mapFragment = rootView.map
        mapFragment.onCreate(savedInstanceState)
        mapFragment.onResume()
        mapFragment.getMapAsync(this)



        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }


        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.settings, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_filter -> {
                createFilterDialog()
            }
            else -> { }
        }
        return true
    }

    fun createFilterDialog(){
        val builder = AlertDialog.Builder(context!!)

        val groups = arrayOf("Global", "NotGlobal")
        val groupsChecked = booleanArrayOf(true, false)

        builder.setMultiChoiceItems(groups, groupsChecked){dialog, which, isChecked ->
            groupsChecked[which] = isChecked
        }


        builder.setTitle("Filter")

        // Set the positive/yes button click listener
        builder.setPositiveButton("OK"){dialog, _ ->
            var list = ArrayList<String>()
            for (i in 0 until groups.size) {
                val checked = groupsChecked[i]
                if (checked) {
                    list.add(groups[i])
                }
            }
            mapController!!.updateMessageMap(list)
        }

        builder.setNeutralButton("Cancel", null)
        builder.create().show()

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mapController = MapController(googleMap, this.context!!)
        locationService.addMap(mapController!!)

        mMap.setMinZoomPreference(mMap.minZoomLevel/4.0f)

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
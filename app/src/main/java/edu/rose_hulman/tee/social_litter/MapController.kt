package edu.rose_hulman.tee.social_litter

import android.app.AlertDialog
import android.content.Context
import android.location.Location
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.message_popup.view.*


class MapController(var map: GoogleMap, var context: Context) {

    var userPos: LatLng = LatLng(10.0, 20.0)
    lateinit var userMarker: Marker
    var markerMap: HashMap<Message, Marker> = HashMap()

    init{
        userMarker = map.addMarker(MarkerOptions().position(userPos).title("Marker in Sydney"))
        userMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.round_gps_fixed_black_18dp))
        Database.addNewMessageListener(this)
        map.setOnMarkerClickListener { marker: Marker? ->
            if (marker == null) {
                return@setOnMarkerClickListener false
            }
            for ((message, mark) in markerMap.entries) {
                if (mark == marker) {

                    var builder = AlertDialog.Builder(context)
                    var rootView = LayoutInflater.from(context).inflate(R.layout.message_popup, null, false)
                    rootView.title.text = message.messageTitle
                    rootView.like_counter.text = message.likes.toString()
                    rootView.message_text.text = message.messageText
                    rootView.group_name.text = message.groupName
                    builder.setView(rootView)
                        .create().show()
                    break
                }
            }
            true
        }
    }

    fun moveUser(pos: LatLng) {
        userPos = pos
        userMarker.remove()
        userMarker = map.addMarker(MarkerOptions().position(userPos).title("Marker in Sydney"))
        userMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.round_gps_fixed_black_18dp))
    }

    fun centerOnUser() {
        map.moveCamera(CameraUpdateFactory.newLatLng(userPos))
    }

    fun moveUser(loc: Location) {
        moveUser(LatLng(loc.latitude, loc.longitude))
    }

    fun addMessageMarker(message: Message) {
        var pos = LatLng(message.location.latitude, message.location.longitude)
        var marker = map.addMarker(MarkerOptions().position(pos).title(message.messageTitle))

        markerMap.put(message, marker)
    }

    fun removeMarker(message: Message) {
        markerMap.remove(message)
    }

}
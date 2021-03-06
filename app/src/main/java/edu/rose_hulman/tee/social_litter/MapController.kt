package edu.rose_hulman.tee.social_litter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Camera
import android.graphics.Color
import android.location.Location
import android.provider.ContactsContract
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.message_popup.*
import kotlinx.android.synthetic.main.message_popup.view.*
import kotlinx.android.synthetic.main.spinner_custom.view.*


class MapController(var map: GoogleMap, var context: Context) {

    var userPos: LatLng = LatLng(10.0, 20.0)
    lateinit var userMarker: Marker
    var markerMap: HashMap<Message, Marker> = HashMap()
    var messageMap = MessageMap()
    var filterList = ArrayList<String>()
    var firstPosSet = true
    var following = true

    init{
        userMarker = map.addMarker(MarkerOptions().position(userPos).title("Marker in Sydney"))
        userMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.round_gps_fixed_black_18dp))
        Database.getAllMessagesForAllGroups(this)
        Database.addNewMessageListener(this)
        filterList = Database.user!!.groupsFilter
        map.setOnCameraMoveStartedListener {reason: Int ->
            if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                following = false
            }
        }
        //centerOnUser()
        map.setOnMarkerClickListener { marker: Marker? ->
            if (marker == null) {
                return@setOnMarkerClickListener false
            }
            for(list in messageMap.getMap().values){
                for(pair in list){
                    if(pair.marker == marker){
                        val message = pair.message

                        val result = floatArrayOf(0.0f)
                        Location.distanceBetween(userPos.latitude, userPos.longitude, marker.position.latitude, marker.position.longitude, result)
                        if(result[0] <= message.radius){
                            var builder = AlertDialog.Builder(context)
                            var rootView = LayoutInflater.from(context).inflate(R.layout.message_popup, null, false)

                            rootView.title.setText(message.messageTitle)
                            rootView.user_name.text = message.originalUser.toString()
                            rootView.message_text.setText(message.messageText)
                            rootView.group_name.text = message.groupName

                            if (message.originalUser.equals(Database.user?.username)) {
                                rootView.message_text.setInputType(InputType.TYPE_CLASS_TEXT)
                                rootView.message_text.setEnabled(true)
                                rootView.title.setInputType(InputType.TYPE_CLASS_TEXT)
                                rootView.title.setEnabled(true)
                                builder.setPositiveButton("Submit") { _,_ ->
                                    message.messageText = rootView.message_text.text.toString()
                                    message.messageTitle = rootView.title.text.toString()
                                    Database.modifyMessage(message)
                                }
                                builder.setNegativeButton("Delete") { _,_ ->
                                    Database.deleteMessage(message)
                                }
                                builder.setNeutralButton("Cancel", null)
                            }

                            builder.setView(rootView)
                                .create().show()
                            break
                        }
                    }
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
//        map.addCircle(CircleOptions().center(userPos).radius(10000.0)
//            .fillColor(Color.rgb(255,200,200)).strokeColor(Color.rgb(200, 150, 150)))
        if (firstPosSet) {
            firstPosSet = false
            centerOnUser()
        }
    }

    fun centerOnUser() {
        //map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(userPos.latitude, userPos.longitude), 17.0f))
        map.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder().tilt(45.0f).target(LatLng(userPos.latitude, userPos.longitude))
            .zoom(18.0f).build()))
        this.following = true
    }

    fun zoomIn() {
        map.animateCamera(CameraUpdateFactory.zoomIn())
    }

    fun zoomOut() {
        map.animateCamera(CameraUpdateFactory.zoomOut())

    }

    fun moveUser(loc: Location) {
        moveUser(LatLng(loc.latitude, loc.longitude))
        if (following) {
            centerOnUser()
        }
    }

    fun addMessageMarker(message: Message) {
        var pos = LatLng(message.location.latitude, message.location.longitude)
        var marker = map.addMarker(MarkerOptions().position(pos).title(message.messageTitle))

        markerMap.put(message, marker)
    }

    fun removeMarker(message: Message) {
        markerMap.remove(message)
    }


    fun setFilter(filter : ArrayList<String>){
        filterList.clear()
        filterList.addAll(filter)
        Database.setFilters(filter)
    }

    fun updateMessageMap(){
        messageMap.setGroups(filterList, map)
    }

    fun updateMessageMap(groupName : String, message : Message){
        messageMap.addMessage(groupName, message, map)
    }

    fun deleteMessage(groupName: String, message: Message) {
        messageMap.deleteMessage(groupName, message)
    }

}
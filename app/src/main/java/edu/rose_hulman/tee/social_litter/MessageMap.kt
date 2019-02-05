package edu.rose_hulman.tee.social_litter

import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MessageMap {

    private var map = HashMap<String, ArrayList<MessageMarker>>()

    fun setGroups(groups : ArrayList<String>, gMap : GoogleMap){
        var keyRemoval = ArrayList<String>()
        for(key in map.keys){
            if(!groups.contains(key)){
                removeMarkers(key)
                keyRemoval.add(key)
            }
        }
        for(key in keyRemoval){
            map.remove(key)
        }

        for(str in groups){
            if(!map.containsKey(str)){
                addMarkers(str, gMap)
            }
        }
    }


    fun removeMarkers(key : String){
        val list = map[key]
        if(list != null) {
            for (marker in list) {
                marker.remove()
            }
        }
    }

    fun addMarkers(key : String, gMap : GoogleMap){
        map[key] = ArrayList()
        val newList = Database.getMessageList(key)
        if(newList != null) {
            for (message in newList) {
                addMessage(key, message, gMap)
            }
        }
    }

    fun addMessage(key : String, message : Message, gMap : GoogleMap){
        val position = LatLng(message.location.latitude, message.location.longitude)
        if(map[key] != null) {
            val marker = gMap.addMarker(MarkerOptions().position(position).title(message.messageTitle))
            var circle = gMap.addCircle(CircleOptions().center(position).radius(message.radius).strokeColor(R.color.blue_6))
            map[key]!!.add(MessageMarker(message, marker, circle))
        }
    }

    fun getMap() : HashMap<String, ArrayList<MessageMarker>>{
        return map
    }

}
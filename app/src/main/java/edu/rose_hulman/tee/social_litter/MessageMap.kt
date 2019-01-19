package edu.rose_hulman.tee.social_litter

import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MessageMap {

    private var map = HashMap<String, ArrayList<Marker>>()

    fun setGroups(groups : ArrayList<String>, gMap : GoogleMap){
        for(key in map.keys){
            if(!groups.contains(key)){
                removeMarkers(key)
            }
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

            map.remove(key)
        }
    }

    fun addMarkers(key : String, gMap : GoogleMap){
        map[key] = ArrayList()
        val newList = Database.getMessageList(key)
        if(newList != null) {
            for (message in newList) {
                val position = LatLng(message.location.latitude, message.location.longitude)
                val marker = gMap.addMarker(MarkerOptions().position(position).title(message.messageTitle))


                map[key]!!.add(marker)
            }
        }
    }

}
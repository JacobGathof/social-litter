package edu.rose_hulman.tee.social_litter

import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.Marker

data class MessageMarker(var message: Message, var marker : Marker, var radius: Circle) {

    fun remove() {
        marker.remove()
        radius.remove()
    }

}
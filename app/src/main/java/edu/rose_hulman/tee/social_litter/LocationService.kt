package edu.rose_hulman.tee.social_litter

import android.location.Location
import android.location.LocationListener
import android.location.LocationProvider
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.model.LatLng


//The Location Service was created using the following tutorial
//https://developer.android.com/guide/topics/location/strategies
class LocationService() : LocationListener, Parcelable{


    var map: MapController? = null
    var location: Location? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun onLocationChanged(location: Location?) {
        if (location != null) {
            map?.moveUser(location)
            this.location = location
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {

    }

    fun addMap(map: MapController) {
        this.map = map
    }

    fun getLocation() : LatLng {
        if (location != null) {
            return LatLng(location!!.latitude, location!!.longitude)
        }
        return LatLng(10.0, 10.0)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LocationService> {
        override fun createFromParcel(parcel: Parcel): LocationService {
            return LocationService(parcel)
        }

        override fun newArray(size: Int): Array<LocationService?> {
            return arrayOfNulls(size)
        }
    }


}
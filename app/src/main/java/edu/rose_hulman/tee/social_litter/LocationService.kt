package edu.rose_hulman.tee.social_litter

import android.location.Location
import android.location.LocationListener
import android.os.Bundle


//The Location Service was created using the following tutorial
//https://developer.android.com/guide/topics/location/strategies
class LocationService : LocationListener {
    override fun onLocationChanged(location: Location?) {
        if (location != null) {

        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderEnabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderDisabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
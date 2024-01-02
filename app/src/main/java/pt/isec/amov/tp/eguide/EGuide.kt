package pt.isec.amov.tp.eguide

import android.app.Application
import com.google.android.gms.location.LocationServices
import pt.isec.amov.tp.eguide.utils.location.FusedLocationHandler
import pt.isec.amov.tp.eguide.utils.location.LocationHandler


class EGuide : Application() {
    /*  val locationHandler : LocationHandler by lazy {
            val locationManager = getSystem     Service(LOCATION_SERVICE) as LocationManager
            LocationManagerHandler(locationManager)
        }*/

    val locationHandler : LocationHandler by lazy {
        val locationProvider = LocationServices.getFusedLocationProviderClient(this)
        FusedLocationHandler(locationProvider)
    }

}
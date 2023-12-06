package pt.isec.amov.tp.eguide.ui.viewmodels

import android.location.Location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import pt.isec.amov.tp.eguide.utils.location.LocationHandler

class LocationViewModelFactory(private val locationHandler:LocationHandler)
    :ViewModelProvider.Factory{
    override fun<T :ViewModel>create(modelClass:Class<T>):T{
        @Suppress("UNCHECKED_CAST")
        return LocationViewModel(locationHandler)as T
    }
}

data

class Coordinates(val team:String, val latitude :Double, val longitude:Double)

class LocationViewModel(private val locationHandler:LocationHandler) :ViewModel(){

    val isLogged = MutableLiveData(false)
    val POIs=listOf(
        Coordinates("Liverpool",53.430819,-2.960828)
    )
    // Permissions
    var coarseLocationPermission=false
    var fineLocationPermission=false
    var backgroundLocationPermission=false

    private val _currentLocation=MutableLiveData(Location(null))
    val currentLocation:LiveData<Location>
        get()=_currentLocation

    private val locationEnabled:Boolean
        get()=locationHandler.locationEnabled

    init{
        locationHandler.onLocation={location->
            _currentLocation.value=location
        }
    }

    fun startLocationUpdates(){
        if(fineLocationPermission&&coarseLocationPermission){
            locationHandler.startLocationUpdates()
        }
    }

    fun stopLocationUpdates(){
        locationHandler.stopLocationUpdates()
    }

    override fun onCleared(){
        super.onCleared()
        stopLocationUpdates()
    }
}

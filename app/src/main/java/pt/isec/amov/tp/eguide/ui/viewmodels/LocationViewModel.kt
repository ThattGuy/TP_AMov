package pt.isec.amov.tp.eguide.ui.viewmodels

import android.annotation.SuppressLint
import android.location.Location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.runBlocking
import org.osmdroid.util.GeoPoint
import pt.isec.amov.tp.eguide.data.Category
import pt.isec.amov.tp.eguide.data.PointOfInterest
import pt.isec.amov.tp.eguide.utils.firebase.FStorageUtil
import kotlin.math.pow
import pt.isec.amov.tp.eguide.utils.location.LocationHandler


class LocationViewModelFactory(private val locationHandler: LocationHandler) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return LocationViewModel(locationHandler) as T
    }
}

data class Coordinates(val team: String, val latitude: Double, val longitude: Double)

class LocationViewModel(private val locationHandler: LocationHandler) : ViewModel() {

    var locationSelected: pt.isec.amov.tp.eguide.data.Location? =
        null // Location selecionado na lista de locais
    val isLogged = MutableLiveData(false)
    val nearbyPOIs = MutableLiveData<List<PointOfInterest>>(ArrayList())
    val nearbyCategories = MutableLiveData<List<Category>>(ArrayList())
    val nearbyLocations = MutableLiveData<List<String>>(ArrayList())

    // Permissions
    var coarseLocationPermission = false
    var fineLocationPermission = false
    var backgroundLocationPermission = false

    val _currentLocation = MutableLiveData(Location(null))
    val currentLocation: LiveData<Location>
        get() = _currentLocation

    private val locationEnabled: Boolean
        get() = locationHandler.locationEnabled

    init {
        locationHandler.onLocation = { location ->
            _currentLocation.value = location
        }

        FStorageUtil.startObserver { nearbyPOIs.value = nearbyPOIs.value?.plus(it) }
    }

    fun startLocationUpdates() {
        if (fineLocationPermission && coarseLocationPermission) {
            locationHandler.startLocationUpdates()
        }
    }

    fun stopLocationUpdates() {
        locationHandler.stopLocationUpdates()
    }

    override fun onCleared() {
        super.onCleared()
        stopLocationUpdates()
    }

    fun insertCategoryIntoDB(categoryName: String, categoryDescription: String) {
        FStorageUtil.insertCategoryIntoDB(categoryName, categoryDescription)
    }

    fun insertLocationIntoDB(name: String, description: String, coordinates: String) {

        //val location = extrairString(this._currentLocation.value.toString())

        //print("\n\n\n\n localização: " +location)
        FStorageUtil.insertLocationIntoDB(name, description, coordinates)

    }

    fun extrairString(str: String): String? {
        val regex = Regex("fused\\s(.*?)\\shAcc")
        val matchResult = regex.find(str)

        return matchResult?.groups?.get(1)?.value
    }

    fun insertPointOfInterest(
        name: String,
        description: String,
        coordinates: String,
        category: String
    ) {

        FStorageUtil.insertPointOfInterest(
            name,
            description,
            coordinates,
            this.locationSelected,
            category
        )
    }


    fun userApprovesLocation(location: pt.isec.amov.tp.eguide.data.Location, userId: String) {
        runBlocking {
            FStorageUtil.userApprovesLocation(location, userId)
        }
    }

    fun getApprovalsOfLocation(location: pt.isec.amov.tp.eguide.data.Location): ArrayList<String> {
        var toReturn = ArrayList<String>()
        runBlocking {
            toReturn = FStorageUtil.getApprovalsOfLocation(location)
        }
        return toReturn
    }

    fun userApprovesPointOfInterest(pointOfInterest: PointOfInterest, userId: String) {
        runBlocking {
            FStorageUtil.userApprovesPointOfInterest(pointOfInterest, userId)
        }
    }

    fun getApprovalsOfPointOfInterest(pointOfInterest: PointOfInterest): ArrayList<String> {
        var toReturn = ArrayList<String>()
        runBlocking {
            toReturn = FStorageUtil.getApprovalsOfPointOfInterest(pointOfInterest)
        }
        return toReturn
    }

    fun userApprovesCategory(category: Category, userId: String) {
        runBlocking {
            FStorageUtil.userApprovesCategory(category, userId)
        }
    }

    fun getApprovalsOfCategory(category: Category): ArrayList<String> {
        var toReturn = ArrayList<String>()
        runBlocking {
            toReturn = FStorageUtil.getApprovalsOfCategory(category)
        }
        return toReturn
    }

    private val NEARBY_THRESHOLD_METERS = 5000

    fun isPOINearCurrentLocation(poi: Coordinates, currentLocation: GeoPoint): Boolean {
        val poiLocation = GeoPoint(poi.latitude, poi.longitude)
        return true
    }

    private fun calculateDistance(startPoint: GeoPoint, endPoint: GeoPoint): Double {
        val earthRadius = 6371000.0 // meters

        val dLat = Math.toRadians(endPoint.latitude - startPoint.latitude)
        val dLon = Math.toRadians(endPoint.longitude - startPoint.longitude)

        val a = Math.sin(dLat / 2).pow(2) +
                Math.cos(Math.toRadians(startPoint.latitude)) * Math.cos(Math.toRadians(endPoint.latitude)) *
                Math.sin(dLon / 2).pow(2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return earthRadius * c
    }

}

package pt.isec.amov.tp.eguide.ui.viewmodels

import android.location.Location
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.runBlocking
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import pt.isec.amov.tp.eguide.data.Category
import pt.isec.amov.tp.eguide.data.PointOfInterest
import pt.isec.amov.tp.eguide.utils.firebase.FStorageUtil
import pt.isec.amov.tp.eguide.utils.location.LocationHandler


class LocationViewModelFactory(private val locationHandler: LocationHandler) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return LocationViewModel(locationHandler) as T
    }
}

class LocationViewModel(private val locationHandler: LocationHandler) : ViewModel() {

    val pois = MutableLiveData<List<PointOfInterest>>(ArrayList())
    var orderedPois = MutableLiveData<List<PointOfInterest>>(ArrayList())
    val categories = MutableLiveData<List<Category>>(ArrayList())
    val locations = MutableLiveData<List<pt.isec.amov.tp.eguide.data.Location>>(ArrayList())
    val currentLocation = MutableLiveData(Location(null))
    val mapBoundingBox = MutableLiveData(BoundingBox(0.0, 0.0, 0.0, 0.0))

    // Permissions
    var coarseLocationPermission = false
    var fineLocationPermission = false
    var backgroundLocationPermission = false


    init {
        locationHandler.onLocation = { location ->
            currentLocation.value = location
        }

        FStorageUtil.startObserver( collectionName = "POI", onNewValues = { id, poi ->
            poi.name = id

            pois.value = pois.value?.plus(poi)
        }, objectType = PointOfInterest::class.java)

        FStorageUtil.startObserver( collectionName = "Categories", onNewValues = { id, category ->
            category.name = id
            categories.value = categories.value?.plus(category)
        }, objectType = Category::class.java)

        FStorageUtil.startObserver( collectionName = "Locations", onNewValues = { id, location ->
            location.name = id

            locations.value = locations.value?.plus(location)
        }, objectType = pt.isec.amov.tp.eguide.data.Location::class.java)
    }

    fun getCurrentCoordinates(): String {
        return "${currentLocation.value?.latitude ?: 0.0},${currentLocation.value?.longitude ?: 0.0}"
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
        FStorageUtil.insertLocationIntoDB(name, description, coordinates)
    }

    fun insertPointOfInterest(
        name: String,
        description: String,
        coordinates: String,
        category: String,
        location: String,
    ) {
        FStorageUtil.insertPointOfInterest(
            name,
            description,
            coordinates,
            location,
            category
        )
    }

    fun insertPOIImage(imageUri: Uri, imageName: String) {
        FStorageUtil.uploadImage(imageUri, "POI", imageName)
    }

    fun insertCategoryImage(imageUri: Uri, imageName: String) {
        FStorageUtil.uploadImage(imageUri, "Categories", imageName)
    }

    fun insertLocationImages(imageUri: Uri, imageName: String) {
        FStorageUtil.uploadImage(imageUri, "Locations", imageName)
    }

    fun getPOIImage(imageName: String, onResult: (Uri) -> Unit) {
        FStorageUtil.downloadImage(imageName, "POI", onResult)
    }

    fun getCategoryImage(imageName: String, onResult: (Uri) -> Unit) {
        FStorageUtil.downloadImage(imageName, "Categories", onResult)
    }

    fun getLocationImage(imageName: String, onResult: (Uri) -> Unit) {
        FStorageUtil.downloadImage(imageName, "Locations", onResult)
    }

    fun approveCategory(category: Category, userId: String ) {
        FStorageUtil.insertApproval("Categories", category.name!!, userId)
    }


    fun approveLocation(location: pt.isec.amov.tp.eguide.data.Location, userId: String ) {
        FStorageUtil.insertApproval("Locations", location.name!!, userId)
    }

    fun approvePOI(poi: PointOfInterest, userId: String ) {
        FStorageUtil.insertApproval("Locations", poi.name!!, userId)
    }

    private fun orderPOIsByDistance(pois: List<PointOfInterest>, otherPoint: GeoPoint): List<PointOfInterest> {
        return pois.map { poi ->
            val poiGeoPoint = poi.toGeoPoint()
            val distance = poiGeoPoint.distanceToAsDouble(otherPoint)
            Pair(poi, distance)
        }.sortedBy { it.second } // Sort by distance
            .map { it.first } // Extract the sorted POIs
    }

    fun refreshVisiblePois(): List<PointOfInterest> {
        val visiblePois = pois.value!!.filter { mapBoundingBox.value!!.contains(it.toGeoPoint()) }
        val sortedPois = orderPOIsByDistance(visiblePois, mapBoundingBox.value!!.centerWithDateLine)

        orderedPois.value = sortedPois

        return visiblePois
    }



}

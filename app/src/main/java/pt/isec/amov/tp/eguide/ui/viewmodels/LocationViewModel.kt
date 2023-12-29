package pt.isec.amov.tp.eguide.ui.viewmodels

import android.location.Location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.runBlocking
import pt.isec.amov.tp.eguide.data.PointOfInterest
import pt.isec.amov.tp.eguide.utils.firebase.FStorageUtil

import pt.isec.amov.tp.eguide.utils.location.LocationHandler

class LocationViewModelFactory(private val locationHandler: LocationHandler)
    :ViewModelProvider.Factory{
    override fun<T :ViewModel>create(modelClass:Class<T>):T{
        @Suppress("UNCHECKED_CAST")
        return LocationViewModel(locationHandler)as T
    }
}

data class Coordinates(val team:String, val latitude :Double, val longitude:Double)

class LocationViewModel(private val locationHandler: LocationHandler) :ViewModel(){

    var locationSelected : pt.isec.amov.tp.eguide.data.Location? = null // Location selecionado na lista de locais

    val isLogged = MutableLiveData(false)
    val POIs=listOf(
        Coordinates("Liverpool",53.430819,-2.960828)
    )
    // Permissions
    var coarseLocationPermission=false
    var fineLocationPermission=false
    var backgroundLocationPermission=false

     val _currentLocation=MutableLiveData(Location(null))
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

    fun insertCategoryIntoDB(categoryName : String, categoryDescription : String){

       FStorageUtil.insertCategoryIntoDB(categoryName,categoryDescription)
    }

   fun insertLocationIntoDB(name : String,description: String){

       val location = extrairString(this._currentLocation.value.toString())

       print("\n\n\n\n localização: " +location)
       FStorageUtil.insertLocationIntoDB(name,description,location!!)

   }

    //var lista = ArrayList<pt.isec.amov.tp.eguide.data.Location>()
      fun getLocations() : ArrayList<pt.isec.amov.tp.eguide.data.Location>
    {
        var listaToReturn = ArrayList<pt.isec.amov.tp.eguide.data.Location>()


            runBlocking {
                //FStorageUtil.fetchLocations {locations ->  listaToReturn = locations }
                listaToReturn = FStorageUtil.provideLocations()
            }


        return listaToReturn

            // Faça algo com a lista de locais

        //Thread.sleep(2000)
        //return lista

    }


     fun extrairString(str: String): String? {
        val regex = Regex("fused\\s(.*?)\\shAcc")
        val matchResult = regex.find(str)

        return matchResult?.groups?.get(1)?.value
    }

    fun insertPointOfInterest(
        name: String,
        description: String,
        coordinates: String
    ) {

        FStorageUtil.insertPointOfInterest(name,description,coordinates,this.locationSelected)
    }

    fun getPointsOfInterest() : ArrayList<PointOfInterest> {

        var listaToReturn = ArrayList<PointOfInterest>()
        runBlocking {
            listaToReturn = FStorageUtil.providePointsOfInterest(locationSelected?.name)
        }
        return listaToReturn
    }

}

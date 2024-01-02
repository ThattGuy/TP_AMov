package pt.isec.amov.tp.eguide.data

import com.google.firebase.firestore.PropertyName
import org.osmdroid.util.GeoPoint

data class Location(@PropertyName("id")
                    var name: String? = null,
                    @PropertyName("Coordinates")
                    var coordinates: String = "0.0,0.0",
                    @PropertyName("CreatedBy")
                    val createdBy : String? = null,
                    @field:JvmField
                    @PropertyName("IsApproved")
                    var isApproved : Boolean? = false,
                    @PropertyName("ApprovedByUsers")
                    val approvedByUsers: List<String>? = listOf()
){
    fun toGeoPoint(): GeoPoint {
        val latLong = this.coordinates!!.split(",").map { it.toDouble() }

        return GeoPoint(latLong[0], latLong[1])
    }
}
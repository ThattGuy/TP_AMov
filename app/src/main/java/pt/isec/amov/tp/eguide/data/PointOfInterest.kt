package pt.isec.amov.tp.eguide.data

import com.google.firebase.firestore.PropertyName
import org.osmdroid.util.GeoPoint


data class PointOfInterest(
    @PropertyName("id")
    var name: String? = null,
    @PropertyName("Description")
    val description: String? = null,
    @PropertyName("Location")
    val location: String? = null,
    @PropertyName("Coordinates")
    val coordinates: String? = null,
    @PropertyName("CreatedBy")
    val createdBy: String? = "",
    @field:JvmField // use this annotation if your Boolean field is prefixed with 'is'
    @PropertyName("IsApproved")
    var isApproved: Boolean? = false,
    @PropertyName("Likes")
    var likes: Long? = 0,
    @PropertyName("Dislikes")
    var dislikes: Long? = 0,
    @PropertyName("Category")
    val category: String? = null,
    @PropertyName("ImageUrl")
    val imageUrl: String? = null,
    @PropertyName("ApprovedByUsers")
val approvedByUsers: List<String>? = listOf(),
    @PropertyName("PendingDelete")
    var pendingDelete: Boolean? = false,
    @PropertyName("DeletionApprovedBy")
    val deletionApprovedBy: List<String>? = listOf()


) {
    fun toGeoPoint(): GeoPoint {
        val latLong = this.coordinates!!.split(",").map { it.toDouble() }

        return GeoPoint(latLong[0], latLong[1])
    }
}



package pt.isec.amov.tp.eguide.data

import com.google.firebase.firestore.PropertyName

class Category(
    @PropertyName("id")
    var name: String? = null,
               @PropertyName("Description")
               val description : String? = null,
               @PropertyName("CreatedBy")
               val createdBy : String? = null,
               @field:JvmField // use this annotation if your Boolean field is prefixed with 'is'
               @PropertyName("IsApproved")
               var isApproved : Boolean? = false,
    @PropertyName("ApprovedByUsers")
val approvedByUsers: List<String>? = listOf()
)
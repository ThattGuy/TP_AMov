package pt.isec.amov.tp.eguide.data

import com.google.firebase.firestore.PropertyName

data class Review(
    @PropertyName("ReviewTittle")
    var tittle: String? = null,
    @PropertyName("CreatedBy")
    val createdBy: String? = null,
    @PropertyName("Review")
    val review: String? = null,
    @PropertyName("Rating")
    val rating: Long? = null,

    )
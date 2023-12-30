package pt.isec.amov.tp.eguide.data

data class Location(var name: String?,
                   // var address: String?,
                    var coordinates: String,
                    val createdBy : String?,
                    var isApproved : Boolean? = false) {

}

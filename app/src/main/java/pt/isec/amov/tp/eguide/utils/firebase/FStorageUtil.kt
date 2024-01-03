package pt.isec.amov.tp.eguide.utils.firebase

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import pt.isec.amov.tp.eguide.data.Category
import pt.isec.amov.tp.eguide.data.Location
import pt.isec.amov.tp.eguide.data.PointOfInterest
import java.io.File
import java.io.IOException
import java.io.InputStream

class FStorageUtil {
    companion object {

        fun insertCategoryIntoDB(categoryName: String, categoryDescription: String) {
            val db = com.google.firebase.Firebase.firestore
            val nameData = hashMapOf(
                "Name" to categoryName,
                "Description" to categoryDescription,
                "CreatedBy" to FAuthUtil.currentUser?.uid,
                "IsApproved" to false
            )

            db.collection("Categories").document(categoryName).set(nameData).addOnSuccessListener {
                Log.i(ContentValues.TAG, "addDataToFirestore: Success")
            }.addOnFailureListener { e ->
                Log.i(ContentValues.TAG, "addDataToFirestore: ${e.message}")
            }

        }

        fun insertLocationIntoDB(name: String, description: String, locationCoordinates: String) {
            val db = Firebase.firestore
            val userId = FAuthUtil.currentUser?.uid
            val data = hashMapOf(
                "Description" to description,
                "Coordinates" to locationCoordinates,
                "CreatedBy" to userId,
                "IsApproved" to false
            )

            db.collection("Locations").document(name).set(data).addOnSuccessListener {
                Log.i(ContentValues.TAG, "addDataToFirestore: Success")
            }.addOnFailureListener { e ->
                Log.i(ContentValues.TAG, "addDataToFirestore: ${e.message}")
            }
        }

        fun insertPointOfInterest(
            name: String,
            description: String,
            coordinates: String,
            location: String,
            category: String,
        ) {

            val db = Firebase.firestore
            val data = hashMapOf(
                "Description" to description,
                "Coordinates" to coordinates,
                "Location" to location,
                "CreatedBy" to FAuthUtil.currentUser?.uid,
                "Category" to category,
                "IsApproved" to false,
                "Likes" to 0,
                "Dislikes" to 0
            )
            db.collection("POI").document(name).set(data)
                .addOnCompleteListener {
                    Log.e("Firestore", "Point of interest added")
                }
        }


        fun insertApproval(collectionName: String, documentName: String, userId: String) {
            val db = Firebase.firestore

            db.runTransaction {
                val documentRef = db.collection(collectionName).document(documentName)
                val document = it.get(documentRef)
                var approvals: List<String>? = document.get("ApprovedByUsers") as List<String>?
                val isApproved = document.getBoolean("IsApproved")

                if (approvals == null) {
                    approvals = listOf()
                }

                approvals = approvals.plus(userId)
                val shouldApprove = if (approvals.size >= 2) true else isApproved
                val data = mapOf(
                    "ApprovedByUsers" to approvals,
                    "IsApproved" to shouldApprove
                )

                it.update(documentRef, data)

                null
            }
                .addOnSuccessListener {
                    Log.d(
                        "Firestore",
                        "Successfully added $collectionName approval for document $documentName for user $userId"
                    )
                }
                .addOnFailureListener { e ->
                    Log.e(
                        "Firestore",
                        "Failed to add $collectionName approval for document $documentName for user $userId",
                        e
                    )
                }
        }

        private val firebaseObservers: MutableMap<String, ListenerRegistration> = mutableMapOf()

        fun <T> startObserver(
            collectionName: String,
            onNewValues: (String, T) -> Unit,
            objectType: Class<T>
        ) {
            stopObserver(collectionName)
            val db = Firebase.firestore
            val listener = db.collection(collectionName)
                .addSnapshotListener { result, e ->
                    if (result == null || e != null) {
                        Log.e("Firestore", "Error getting $collectionName", e)
                        return@addSnapshotListener
                    }

                    if (result.isEmpty) {
                        Log.e("Firestore", "No $collectionName found")
                        return@addSnapshotListener
                    }

                    for (resultDocument in result) {
                        val convertedDocument = resultDocument.toObject(objectType)
                        onNewValues(resultDocument.id, convertedDocument)
                    }
                }
            firebaseObservers[collectionName] = listener
        }

        private fun stopObserver(collectionName: String) {
            val listener: ListenerRegistration? = firebaseObservers[collectionName]
            listener?.let {
                listener.remove()
                firebaseObservers.remove(collectionName)
            }
        }


        fun uploadImage(imageUri: Uri, imageType: String, imageName: String) {
            val storage = Firebase.storage
            val userId = FAuthUtil.currentUser?.uid

            val pathRef = storage.reference.child("images/$userId/$imageType/$imageName")
            val uploadTask = pathRef.putFile(imageUri)
            uploadTask.addOnFailureListener { e ->
                Log.e(
                    "Firestore",
                    "Failed to upload image $imageType/$imageName($imageUri) for user $userId",
                    e
                )
            }.addOnSuccessListener {
                Log.d(
                    "Firestore",
                    "Uploaded image $imageType/$$imageName($imageUri) for user $userId"
                )
            }
        }

        fun downloadImage(imageName: String, imageType: String, onResult: (Uri) -> Unit) {
            val storage = Firebase.storage
            val userId = FAuthUtil.currentUser?.uid
            val localFile = File.createTempFile("images", "jpg")

            val pathRef = storage.reference.child("images/$userId/$imageType/$imageName")
            val downloadTask = pathRef.getFile(localFile)

            downloadTask.addOnFailureListener { e ->
                Log.e(
                    "Firestore",
                    "Failed to download image $imageType/$imageName for user $userId",
                    e
                )
            }.addOnSuccessListener {
                Log.d("Firestore", "Uploaded image $imageType/$imageName for user $userId")
                onResult(localFile.toUri())
            }
        }

        fun editPointOfInterest(
            name: String,
            description: String?,
            coordinates: String?,
            location: String?,
            category: String?,
        ){
            val db = Firebase.firestore
            val v = db.collection("POI").document(name)
            var wasChanged = false

            db.runTransaction { transaction ->
                val doc = transaction.get(v)
                if (doc.exists()) {
                    if (description != "") {
                        transaction.update(v, "Description", description)
                        wasChanged = true
                    }
                    if (coordinates != "") {
                        transaction.update(v, "Coordinates", coordinates)
                        wasChanged = true
                    }
                    if (location != "") {
                        transaction.update(v, "Location", location)
                        wasChanged = true
                    }
                    if (category != "") {
                        transaction.update(v, "Category", category)
                        wasChanged = true
                    }

                    if (wasChanged) {
                        transaction.update(v, "IsApproved", false)
                    }
                    null
                } else
                    throw FirebaseFirestoreException(
                        "Doesn't exist",
                        FirebaseFirestoreException.Code.UNAVAILABLE
                    )
            }
        }

        fun deletePointOfInterest(editPoiName: String) {
            val db = Firebase.firestore
            val v = db.collection("POI").document(editPoiName)

            v.delete()
                .addOnCompleteListener { result ->
                    Log.e("Firestore", "Point of interest deleted")
                }
        }

        fun editLocation(nameOfEditLocation: String, description: String, coordinates: String){
            val db = Firebase.firestore
            val v = db.collection("Locations").document(nameOfEditLocation)
            var wasChanged = false

            db.runTransaction { transaction ->
                val doc = transaction.get(v)
                if (doc.exists()) {
                    if (description != "") {
                        transaction.update(v, "Description", description)
                        wasChanged = true
                    }
                    if (coordinates != "") {
                        transaction.update(v, "Coordinates", coordinates)
                        wasChanged = true
                    }
                    if (wasChanged) {
                        transaction.update(v, "IsApproved", false)
                    }
                    null
                } else
                    throw FirebaseFirestoreException(
                        "Doesn't exist",
                        FirebaseFirestoreException.Code.UNAVAILABLE
                    )
            }
        }

        fun deleteLocation(nameOfEditLocation: String) {
            val db = Firebase.firestore
            val v = db.collection("Locations").document(nameOfEditLocation)

            v.delete()
                .addOnCompleteListener { result ->
                    Log.e("Firestore", "Location deleted")
                }
        }

        fun editCategory(categoryToEdit: String, categoryDescription: String) {
            val db = Firebase.firestore
            val v = db.collection("Categories").document(categoryToEdit)
            var wasChanged = false

            db.runTransaction { transaction ->
                val doc = transaction.get(v)
                if (doc.exists()) {
                    if (categoryDescription != "") {
                        transaction.update(v, "Description", categoryDescription)
                        wasChanged = true
                    }

                    if (wasChanged) {
                        transaction.update(v, "IsApproved", false)
                    }
                    null
                } else
                    throw FirebaseFirestoreException(
                        "Doesn't exist",
                        FirebaseFirestoreException.Code.UNAVAILABLE
                    )
            }
        }

        fun deleteCategory(categoryToEdit: String) {
            val db = Firebase.firestore
            val v = db.collection("Categories").document(categoryToEdit)

            v.delete()
                .addOnCompleteListener { result ->
                    Log.e("Firestore", "Category deleted")
                }
        }
    }
}
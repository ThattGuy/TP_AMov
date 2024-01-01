package pt.isec.amov.tp.eguide.utils.firebase

import android.content.ContentValues
import android.content.res.AssetManager
import android.util.Log
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import pt.isec.amov.tp.eguide.data.Category
import pt.isec.amov.tp.eguide.data.Location
import pt.isec.amov.tp.eguide.data.PointOfInterest
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

            //val irrelevantData = hashMapOf("Nothing here" to "Nothing here")
            val irrelevantData = hashMapOf(
                "Description" to description,
                "Coordinates" to "coordinates",
                "Location" to "location",
                "CreatedBy" to FAuthUtil.currentUser?.uid,
                "IsApproved" to false
            )
            //Inserir um local
            db.collection("Locations").document(name).set(data).addOnSuccessListener {
                Log.i(ContentValues.TAG, "addDataToFirestore: Success")
            }.addOnFailureListener { e ->
                Log.i(ContentValues.TAG, "addDataToFirestore: ${e.message}")
            }
            //Inserir uma colecao "utilizadors que aprovaram o local"
            /*  db.collection("Locations").document(name).collection("ApprovedByUsers").document(userId.toString()).set(irrelevantData)
                  .addOnSuccessListener {
                      Log.i(ContentValues.TAG, "addDataToFirestore: Success")
                  }
                  .addOnFailureListener { e->
                      Log.i(ContentValues.TAG, "addDataToFirestore: ${e.message}")
                  }
              //Inserir uma colecao Pontos de interesse do local
              db.collection("Locations").document(name).collection("PointsOfInterest").document("ReferencePoint").set(irrelevantData)
                  .addOnSuccessListener {
                      Log.i(ContentValues.TAG, "addDataToFirestore: Success")
                  }
                  .addOnFailureListener { e->
                      Log.i(ContentValues.TAG, "addDataToFirestore: ${e.message}")
                  }

             */
        }



        suspend fun provideLocations(): ArrayList<Location> {
            return try {
                val db = Firebase.firestore
                val lista = ArrayList<Location>()

                val querySnapshot = db.collection("Locations").get().await()
                for (document in querySnapshot.documents) {
                    val location = Location(
                        document.id,
                        //document.data?.get("Description").toString(),
                        document.data?.get("Coordinates").toString(),
                        document.data?.get("CreatedBy").toString(),
                        document.data?.get("IsApproved") as Boolean
                        //document.data["Coordinates"].toString()
                    )
                    lista.add(location)
                }

                lista
            } catch (e: Exception) {
                Log.d(ContentValues.TAG, "Error getting documents: ", e)
                ArrayList()
            }
        }


        fun insertPointOfInterest(
            name: String,
            description: String,
            coordinates: String,
            locationSelected: Location?,
            category: String,
        ) {

            val db = Firebase.firestore

            val likes = 0;
            val dislikes = 0;

            val data = hashMapOf(
                "Description" to description,
                "Coordinates" to coordinates,
                "Location" to locationSelected?.name,
                "CreatedBy" to FAuthUtil.currentUser?.uid,
                "Category" to category,
                "IsApproved" to false,
                "Likes" to likes,
                "Dislikes" to dislikes
            )
            db.collection("POI").document(name).set(data)
                .addOnCompleteListener { result ->
                    Log.e("Firestore", "Point of interest added")
                }
        }

        fun getAllPointsOfInterest(onResult: (PointOfInterest) -> Unit) {
            val db = Firebase.firestore
            val poiTable = db.collection("POI")

            poiTable.get(Source.SERVER).addOnSuccessListener { result ->
                if (result.isEmpty) {
                    Log.e("Firestore", "No points of interest found")
                    return@addOnSuccessListener
                }

                for (resultDocument in result) {
                    val pointOfInterest = PointOfInterest(
                        resultDocument.id,
                        resultDocument.getString("Description") ?: "",
                        resultDocument.getString("Location") ?: "",
                        resultDocument.getString("Coordinates") ?: "",
                        resultDocument.getString("CreatedBy") ?: "",
                        resultDocument.getBoolean("IsApproved") ?: false
                    )

                    onResult(pointOfInterest)
                }
            }.addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting points of interest", exception)
            }
        }

        suspend fun getCityPointOfInterest(name: String): java.util.ArrayList<PointOfInterest> {
            return ArrayList()
        }

        suspend fun provideCategories(): ArrayList<Category> {
            return try {
                val db = Firebase.firestore
                val lista = ArrayList<Category>()

                val querySnapshot = db.collection("Categories").get().await()
                for (document in querySnapshot.documents) {
                    val category = Category(
                        document.id,
                        document.data?.get("Description").toString(),
                        document.data?.get("CreatedBy").toString(),
                        document.data?.get("IsApproved") as Boolean
                    )

                    lista.add(category)
                }

                lista
            } catch (e: Exception) {
                Log.d(ContentValues.TAG, "Error getting documents: ", e)
                ArrayList()
            }
        }


        suspend fun getApprovalsOfLocation(location: Location): ArrayList<String> {
            val db = Firebase.firestore
            var listToReturn = ArrayList<String>()
            val querySnapshot = db.collection("Locations").document(location.name.toString())
                .collection("ApprovedByUsers").get().await()

            for (user in querySnapshot.documents) {
                listToReturn.add(user.id)
            }
            return listToReturn
        }

        suspend fun userApprovesLocation(location: Location, userId: String) {
            val db = Firebase.firestore
            var auxList = ArrayList<String>()
            val data = hashMapOf(
                "UserId" to userId
            )

            db.collection("Locations").document(location.name.toString())
                .collection("ApprovedByUsers").document(userId).set(data).addOnSuccessListener {
                    Log.i(ContentValues.TAG, "addDataToFirestore: Success")
                }.addOnFailureListener { e ->
                    Log.i(ContentValues.TAG, "addDataToFirestore: ${e.message}")
                }
            //recolher lista de ApprovedByUsers

            val querySnapshot = db.collection("Locations").document(location.name.toString())
                .collection("ApprovedByUsers").get().await()

            for (user in querySnapshot.documents) {
                auxList.add(user.id)
            }
            //if(querySnapshot.documents.size >= 4)
            if (auxList.size >= 2) {
                db.collection("Locations").document(location.name.toString())
                    .update("IsApproved", true).addOnSuccessListener {
                        Log.i(ContentValues.TAG, "addDataToFirestore: Success")
                    }.addOnFailureListener { e ->
                        Log.i(ContentValues.TAG, "addDataToFirestore: ${e.message}")
                    }
            }

        }

        suspend fun userApprovesPointOfInterest(pointOfInterest: PointOfInterest, userId: String) {
            val db = Firebase.firestore
            val data = hashMapOf("UserId" to userId)
            var auxList = ArrayList<String>()

            db.collection("POI").document(pointOfInterest.name.toString())
                .collection("ApprovedByUsers").document(userId).set(data).addOnSuccessListener {
                    Log.i(ContentValues.TAG, "addDataToFirestore: Success")
                }.addOnFailureListener { e ->
                    Log.i(ContentValues.TAG, "addDataToFirestore: ${e.message}")
                }

            // Collect list of ApprovedByUsers
            val querySnapshot = db.collection("POI").document(pointOfInterest.name.toString())
                .collection("ApprovedByUsers").get().await()

            for (user in querySnapshot.documents) {
                auxList.add(user.id)
            }

            if (auxList.size >= 2) {
                db.collection("POI").document(pointOfInterest.name.toString())
                    .update("IsApproved", true).addOnSuccessListener {
                        Log.i(ContentValues.TAG, "addDataToFirestore: Success")
                    }.addOnFailureListener { e ->
                        Log.i(ContentValues.TAG, "addDataToFirestore: ${e.message}")
                    }
            }
        }

        suspend fun getApprovalsOfPointOfInterest(pointOfInterest: PointOfInterest): ArrayList<String> {
            val db = Firebase.firestore
            val listToReturn = ArrayList<String>()
            val querySnapshot =
                db.collection("POI").document(pointOfInterest.name.toString())
                    .collection("ApprovedByUsers").get().await()

            for (user in querySnapshot.documents) {
                listToReturn.add(user.id)
            }
            return listToReturn
        }

        suspend fun userApprovesCategory(category: Category, userId: String) {
            val db = Firebase.firestore
            val data = hashMapOf(
                "UserId" to userId
            )
            var auxList = ArrayList<String>()

            db.collection("Categories").document(category.name.toString())
                .collection("ApprovedByUsers").document(userId).set(data).addOnSuccessListener {
                    Log.i(ContentValues.TAG, "addDataToFirestore: Success")
                }.addOnFailureListener { e ->
                    Log.i(ContentValues.TAG, "addDataToFirestore: ${e.message}")
                }
            //recolher lista de ApprovedByUsers

            val querySnapshot = db.collection("Categories").document(category.name.toString())
                .collection("ApprovedByUsers").get().await()

            for (user in querySnapshot.documents) {
                auxList.add(user.id)
            }
            if (auxList.size >= 2) {
                db.collection("Categories").document(category.name.toString())
                    .update("IsApproved", true).addOnSuccessListener {
                        Log.i(ContentValues.TAG, "addDataToFirestore: Success")
                    }.addOnFailureListener { e ->
                        Log.i(ContentValues.TAG, "addDataToFirestore: ${e.message}")
                    }
            }
        }

        suspend fun getApprovalsOfCategory(category: Category): java.util.ArrayList<String> {
            val db = Firebase.firestore
            val listToReturn = ArrayList<String>()
            val querySnapshot = db.collection("Categories").document(category.name.toString())
                .collection("ApprovedByUsers").get().await()
            for (document in querySnapshot.documents) {
                listToReturn.add(document.id)

            }

            return listToReturn
        }

        private var listenerRegistration: ListenerRegistration? = null
        fun startObserver(onNewValues: (PointOfInterest) -> Unit) {
            stopObserver()
            val db = Firebase.firestore
            listenerRegistration = db.collection("POI")
                .addSnapshotListener{ result, e ->
                    if(result == null || e != null){
                        Log.e("Firestore", "Error getting points of interest", e)
                        return@addSnapshotListener
                    }

                    if (result.isEmpty) {
                        android.util.Log.e("Firestore", "No points of interest found")
                        return@addSnapshotListener
                    }

                    for (resultDocument in result) {
                        val pointOfInterest = pt.isec.amov.tp.eguide.data.PointOfInterest(
                            resultDocument.id,
                            resultDocument.getString("Description") ?: "",
                            resultDocument.getString("Location") ?: "",
                            resultDocument.getString("Coordinates") ?: "",
                            resultDocument.getString("CreatedBy") ?: "",
                            resultDocument.getBoolean("IsApproved") ?: false
                        )

                        onNewValues(pointOfInterest)
                    }
                }
        }

        fun stopObserver() {
            listenerRegistration?.remove()
        }


        fun getFileFromAsset(assetManager: AssetManager, strName: String): InputStream? {
            var istr: InputStream? = null
            try {
                istr = assetManager.open(strName)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return istr
        }

//https://firebase.google.com/docs/storage/android/upload-files

        fun uploadFile(inputStream: InputStream, imgFile: String) {
            val storage = Firebase.storage
            val ref1 = storage.reference
            val ref2 = ref1.child("images")
            val ref3 = ref2.child(imgFile)

            val uploadTask = ref3.putStream(inputStream)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref3.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    println(downloadUri.toString())
                } else {
                    // Handle failures
                    // ...
                }
            }


        }

        /*
        fun updateDataInFirestoreTrans(onResult: (Throwable?) -> Unit) {
            val db = Firebase.firestore
            val v = db.collection("Scores").document("Level1")

            db.runTransaction { transaction ->
                val doc = transaction.get(v)
                if (doc.exists()) {
                    val newnrgames = (doc.getLong("nrgames") ?: 0) + 1
                    val newtopscore = (doc.getLong("topscore") ?: 0) + 100
                    transaction.update(v, "nrgames", newnrgames)
                    transaction.update(v, "topscore", newtopscore)
                    null
                } else
                    throw FirebaseFirestoreException(
                        "Doesn't exist",
                        FirebaseFirestoreException.Code.UNAVAILABLE
                    )
            }.addOnCompleteListener { result ->
                onResult(result.exception)
            }
        }

        fun removeDataFromFirestore(onResult: (Throwable?) -> Unit) {
            val db = Firebase.firestore
            val v = db.collection("Scores").document("Level1")

            v.delete()
                .addOnCompleteListener { onResult(it.exception) }
        }

        private var listenerRegistration: ListenerRegistration? = null

        fun startObserver(onNewValues: (Long, Long) -> Unit) {
            stopObserver()
            val db = Firebase.firestore
            listenerRegistration = db.collection("Scores").document("Level1")
                .addSnapshotListener { docSS, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    if (docSS != null && docSS.exists()) {
                        val nrgames = docSS.getLong("nrgames") ?: 0
                        val topscore = docSS.getLong("topscore") ?: 0
                        Log.i("Firestore", "$nrgames : $topscore")
                        onNewValues(nrgames, topscore)
                    }
                }
        }

        fun stopObserver() {
            listenerRegistration?.remove()
        }

// Storage

        fun getFileFromAsset(assetManager: AssetManager, strName: String): InputStream? {
            var istr: InputStream? = null
            try {
                istr = assetManager.open(strName)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return istr
        }

//https://firebase.google.com/docs/storage/android/upload-files

        fun uploadFile(inputStream: InputStream, imgFile: String) {
            val storage = Firebase.storage
            val ref1 = storage.reference
            val ref2 = ref1.child("images")
            val ref3 = ref2.child(imgFile)

            val uploadTask = ref3.putStream(inputStream)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref3.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    println(downloadUri.toString())
                    // something like:
                    //   https://firebasestorage.googleapis.com/v0/b/p0405ansamov.appspot.com/o/images%2Fimage.png?alt=media&token=302c7119-c3a9-426d-b7b4-6ab5ac25fed9
                } else {
                    // Handle failures
                    // ...
                }
            }


        }*/

        /*
               fun fetchLocations(callback: (ArrayList<Location>) -> Unit) {
                   val db = Firebase.firestore

                   db.collection("Locations").get()
                       .addOnSuccessListener { result ->
                           val lista = ArrayList<Location>()

                           for (document in result) {
                               val location = Location(
                                   document.id,
                                   document.data["Description"].toString(),
                                   document.data["Coordinates"].toString()
                               )
                               lista.add(location)
                           }

                           callback(lista) // Chamando o callback com a lista de locais
                       }
                       .addOnFailureListener { exception ->
                           Log.d(ContentValues.TAG, "Error getting documents: ", exception)
                           callback(ArrayList()) // Em caso de falha, chamando o callback com uma lista vazia
                       }
               }
               */

        /*fun provideLocations() : ArrayList<Location>{


            val db = Firebase.firestore
            val lista = ArrayList<Location>()
           val location = db.collection("Locations").get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val location = Location(document.id,document.data["Description"].toString(),document.data["Coordinates"].toString())
                        lista.add(location)
                    }
                    return@addOnSuccessListener lista

                }
                .addOnFailureListener { exception ->

                Log.d(ContentValues.TAG, "\n\n\nError getting documents: ", exception)

                }
//retiurn lista
            return lista

        }

         */
    }
}
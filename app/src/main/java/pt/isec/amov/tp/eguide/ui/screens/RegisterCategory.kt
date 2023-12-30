package pt.isec.amov.tp.eguide.ui.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel

@Composable
fun RegisterCategory(viewModel: LocationViewModel){
    //val db = Firebase.firestore

    Column(verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally) {

        var categoryName by rememberSaveable { mutableStateOf("")}
        var categoryDescription by rememberSaveable { mutableStateOf("")}
        TextField(value = categoryName, onValueChange = { categoryName = it } , label = {Text(
            stringResource(id = pt.isec.amov.tp.eguide.R.string.category_name))})
        TextField(value = categoryDescription, onValueChange = { categoryDescription = it } , label = {Text(
            stringResource(id = pt.isec.amov.tp.eguide.R.string.category_description))})
        Button(onClick = {
            print("\n\n\n" + categoryName)
            viewModel.insertCategoryIntoDB(categoryName,categoryDescription)
            /*val nameData = hashMapOf("Name" to categoryName, "Description" to categoryDescription)


            db.collection("Categories").document(categoryName).set(nameData)
                .addOnSuccessListener {
                    Log.i(TAG, "addDataToFirestore: Success")
                }
                .addOnFailureListener { e->
                    Log.i(TAG, "addDataToFirestore: ${e.message}")
                }
*/

        }) {
            Text(text = "Save")
        }
    }
}

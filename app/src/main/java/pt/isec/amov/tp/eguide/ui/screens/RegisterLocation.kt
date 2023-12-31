package pt.isec.amov.tp.eguide.ui.screens


import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateLocationScreen(viewModel: LocationViewModel) {


    var description by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("")}
    var coordinates by rememberSaveable {
        mutableStateOf("")
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextField(value = name, onValueChange = {name = it}, placeholder = { Text( stringResource(id = pt.isec.amov.tp.eguide.R.string.name))})
        TextField(value = description, onValueChange = {description = it}, placeholder = { Text(text = "Descrição")})
        TextField(value = coordinates, onValueChange = {coordinates = it},label = { Text(stringResource(id = pt.isec.amov.tp.eguide.R.string.coordinates))})
        Button(onClick = { coordinates =  viewModel.extrairString(viewModel.currentLocation.value.toString())!!}) {
            Text(stringResource(id = pt.isec.amov.tp.eguide.R.string.get_coordinates))
        }

        Button(onClick = {/*TODO*/}){
            Text(stringResource(id = pt.isec.amov.tp.eguide.R.string.select_image))
        }

        Button(onClick = {
            print("\n\n\n Vim aquiii")
            location = viewModel._currentLocation.value.toString()
            viewModel.insertLocationIntoDB(name,description,coordinates)


        }){
            Text(stringResource(id = pt.isec.amov.tp.eguide.R.string.save))
        }



    }




}

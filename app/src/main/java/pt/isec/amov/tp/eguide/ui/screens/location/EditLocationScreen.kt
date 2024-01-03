package pt.isec.amov.tp.eguide.ui.screens.location

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import pt.isec.amov.tp.eguide.R
import pt.isec.amov.tp.eguide.ui.screens.Screens
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel


@Composable
fun EditLocation(viewModel: LocationViewModel, navController: NavController) {

    var description by rememberSaveable { mutableStateOf("") }
    var coordinates by rememberSaveable {
        mutableStateOf("")
    }

    if (coordinates.isBlank()) {
        coordinates = navController.currentBackStackEntry?.arguments?.getString("coordinates") ?: ""
    }

    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text(text = "Descript") })
        TextField(
            value = coordinates,
            onValueChange = { coordinates = it },
            label = { Text(stringResource(id = R.string.coordinates)) })
        Button(onClick = { coordinates = viewModel.getCurrentCoordinates() }) {
            Text(stringResource(id = R.string.get_coordinates))
        }

        Button(
            onClick = {
                viewModel.editLocation(description, coordinates)
                navController.navigate(Screens.LIST_LOCATIONS.route)
            },
            enabled = true
        ) {
            Text(stringResource(id = R.string.save))
        }


        Button(
            onClick = {
                viewModel.deleteLocation()
                navController.navigate(Screens.LIST_LOCATIONS.route)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            ),
            enabled = true
        ) {
            Text(stringResource(id = R.string.delete))
        }

    }

}

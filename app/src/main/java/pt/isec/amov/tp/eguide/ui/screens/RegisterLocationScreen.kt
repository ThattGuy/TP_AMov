package pt.isec.amov.tp.eguide.ui.screens


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import pt.isec.amov.tp.eguide.R
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel


@Composable
fun RegisterLocationScreen(viewModel: LocationViewModel, navController: NavController) {

    var description by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("")}
    var coordinates by rememberSaveable {
        mutableStateOf("")
    }

    if(coordinates.isBlank()) {
        coordinates = navController.currentBackStackEntry?.arguments?.getString("coordinates") ?: ""
    }

    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    // Validation check to enable or disable the save button
    val isFormValid = name.isNotBlank() && description.isNotBlank() && coordinates.isNotBlank() && imageUri != null
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextField(value = name, onValueChange = {name = it}, placeholder = { Text( stringResource(id = pt.isec.amov.tp.eguide.R.string.name))})
        if (name.isBlank()) {
            Text(stringResource(id = R.string.name), color = Color.Red)
        }
        TextField(value = description, onValueChange = {description = it}, placeholder = { Text(text = "Descript")})
        if (description.isBlank()) {
            Text(stringResource(id = R.string.description_is_required), color = Color.Red)
        }
        TextField(value = coordinates, onValueChange = {coordinates = it},label = { Text(stringResource(id = pt.isec.amov.tp.eguide.R.string.coordinates))})
        Button(onClick = { coordinates = viewModel.getCurrentCoordinates()}) {
            Text(stringResource(id = pt.isec.amov.tp.eguide.R.string.get_coordinates))
        }
        if (coordinates.isBlank()) {
            Text(stringResource(id = R.string.coordinates_is_required), color = Color.Red)
        }

        Button(onClick = { pickImageLauncher.launch("image/*") }) {
            Text(stringResource(id = R.string.select_image))
        }
        imageUri?.let { uri ->
            SubcomposeAsyncImage(
                model = uri,
                loading = {
                    CircularProgressIndicator()
                },
                contentDescription = stringResource(id = R.string.select_image)
            )
        }

        Button(onClick = {
            viewModel.insertLocationIntoDB(name,description,coordinates)
            viewModel.insertLocationImages(imageUri!!, name)
            navController.navigate(Screens.LIST_LOCATIONS.route)
        }, enabled = isFormValid) {
            Text(stringResource(id = pt.isec.amov.tp.eguide.R.string.save))
        }

        if (!isFormValid) {
            Text(stringResource(id = R.string.fill_all_fields), color = Color.Red)
        }
    }

}

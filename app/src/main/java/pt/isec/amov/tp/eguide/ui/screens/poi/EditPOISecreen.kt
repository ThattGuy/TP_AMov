package pt.isec.amov.tp.eguide.ui.screens.poi

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import pt.isec.amov.tp.eguide.R
import pt.isec.amov.tp.eguide.ui.screens.Screens
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel

@Composable
fun EditPointOfInterest(navController: NavController, viewModel: LocationViewModel) {
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var coordinates by rememberSaveable { mutableStateOf("") }
    var categoryExpanded by rememberSaveable { mutableStateOf(false) }
    var locationExpanded by rememberSaveable { mutableStateOf(false) }
    var categorySelected by rememberSaveable { mutableStateOf("Categoria") }
    var locationSelected by rememberSaveable {
        mutableStateOf("Location")
    }

    val listAuxCat = viewModel.categories.observeAsState(initial = listOf())
    val listAuxLoc = viewModel.locations.observeAsState(initial = listOf())

    if(coordinates.isBlank()) {
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

        TextField(value = description, onValueChange = { description = it }, label = { Text(stringResource(id = R.string.description)) })


        TextField(value = coordinates, onValueChange = { coordinates = it }, label = { Text(stringResource(id = R.string.coordinates)) })

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

        Button(onClick = { coordinates = viewModel.getCurrentCoordinates() }) {
            Text(stringResource(id = R.string.get_coordinates))
        }

        Box {
            TextButton(onClick = { categoryExpanded = true }) {
                Text(text = categorySelected)
                Icon(Icons.Default.ArrowDropDown, contentDescription = "")
            }
        }

        DropdownMenu(expanded = categoryExpanded, onDismissRequest = { categoryExpanded = false }) {
            listAuxCat.value.forEach { item ->
                DropdownMenuItem(text = { Text(text = item.name.toString()) }, onClick = {
                    categoryExpanded = false
                    categorySelected = item.name.toString()
                })
            }
        }

        Box {
            TextButton(onClick = { locationExpanded = true }) {
                Text(text = locationSelected)
                Icon(Icons.Default.ArrowDropDown, contentDescription = "")
            }
        }

        DropdownMenu(expanded = locationExpanded, onDismissRequest = { locationExpanded = false }) {
            listAuxLoc.value.forEach { item ->
                DropdownMenuItem(text = { Text(text = item.name.toString()) }, onClick = {
                    locationExpanded = false
                    locationSelected = item.name.toString()
                })
            }
        }


        Button(
            onClick = {
                viewModel.editPointOfInterest(description, coordinates, categorySelected, locationSelected)
                navController.navigate(Screens.LIST_POINTS_OF_INTEREST.route)
            },
            enabled = true
        ) {
            Text(stringResource(id = R.string.save))
        }


        Button(
            onClick = {
                viewModel.deletePointOfInterest()
                navController.navigate(Screens.LIST_POINTS_OF_INTEREST.route)
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




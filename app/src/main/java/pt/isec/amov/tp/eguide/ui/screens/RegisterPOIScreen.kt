package pt.isec.amov.tp.eguide.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel

@Composable
fun RegisterPointOfInterest(navController: NavController, viewModel: LocationViewModel) {
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var coordinates by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var categorySelected by rememberSaveable { mutableStateOf("Categoria") }

    val listAuxCat = viewModel.getCategoriesList()
    val listAuxLoc = viewModel.getLocations()

    coordinates = navController.currentBackStackEntry?.arguments?.getString("coordinates") ?: ""

    // Validation check to enable or disable the save button
    val isFormValid = name.isNotBlank() && description.isNotBlank() && coordinates.isNotBlank() && categorySelected != "Categoria"

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(value = name, onValueChange = { name = it }, label = { Text(stringResource(id = pt.isec.amov.tp.eguide.R.string.name)) })
        if (name.isBlank()) {
            Text("Name is required", color = Color.Red)
        }

        TextField(value = description, onValueChange = { description = it }, label = { Text(stringResource(id = pt.isec.amov.tp.eguide.R.string.description)) })
        if (description.isBlank()) {
            Text("Description is required", color = Color.Red)
        }

        TextField(value = coordinates, onValueChange = { coordinates = it }, label = { Text(stringResource(id = pt.isec.amov.tp.eguide.R.string.coordinates)) })
        if (coordinates.isBlank()) {
            Text("Coordinates are required", color = Color.Red)
        }

        Button(onClick = { coordinates = viewModel.extrairString(viewModel.currentLocation.value.toString()) ?: "" }) {
            Text(stringResource(id = pt.isec.amov.tp.eguide.R.string.get_coordinates))
        }

        Box {
            TextButton(onClick = { expanded = true }) {
                Text(text = categorySelected)
                Icon(Icons.Default.ArrowDropDown, contentDescription = "")
            }
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            listAuxCat.forEach { item ->
                DropdownMenuItem(text = { Text(text = item.name.toString()) }, onClick = {
                    expanded = false
                    categorySelected = item.name.toString()
                })
            }
        }
        if (categorySelected == "Categoria") {
            Text("Category is required", color = Color.Red)
        }

        Button(
            onClick = {
                viewModel.insertPointOfInterest(name, description, coordinates, categorySelected)
                navController.navigate(Screens.LIST_POINTS_OF_INTEREST.route)
            },
            enabled = isFormValid  // Button is enabled only if the form is valid
        ) {
            Text(stringResource(id = pt.isec.amov.tp.eguide.R.string.save))
        }

        // Additional feedback if the form is not valid
        if (!isFormValid) {
            Text("Please fill all fields to continue", color = Color.Red)
        }
    }
}




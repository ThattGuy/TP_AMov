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
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel

@Composable
fun RegisterPointOfInterest( navController: NavController,viewModel: LocationViewModel){

    var name by rememberSaveable {
        mutableStateOf("")
    }
    var description by rememberSaveable {
        mutableStateOf("")
    }
    var coordinates by rememberSaveable {
        mutableStateOf("")
    }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var categorySelected by rememberSaveable { mutableStateOf("Categoria") }

    val listAux = viewModel.getCategoriesList()



    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )  {
        TextField(value = name, onValueChange = {name = it},label = { Text(stringResource(id = pt.isec.amov.tp.eguide.R.string.name))})
        TextField(value = description, onValueChange = {description = it},label = { Text(stringResource(id = pt.isec.amov.tp.eguide.R.string.description))})
        TextField(value = coordinates, onValueChange = {coordinates = it},label = { Text(stringResource(id = pt.isec.amov.tp.eguide.R.string.coordinates))})
        Button(onClick = { coordinates =  viewModel.extrairString(viewModel.currentLocation.value.toString())!!}) {
            Text(stringResource(id = pt.isec.amov.tp.eguide.R.string.get_coordinates))
        }

        //Menu que permite escolher uma categoria
        Box{
            TextButton(onClick = { expanded = true }) {
                Text(text = categorySelected)
                Icon(Icons.Default.ArrowDropDown,contentDescription = "")
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = {expanded = false}) {
            listAux.forEach {item ->
                DropdownMenuItem(text = { Text(text = item.name.toString()) }, onClick = {
                    expanded = false
                    categorySelected = item.name.toString()
                })


            }
        }
        Button(onClick = {
            //var location = viewModel.locationSelected
            if(categorySelected == "Category" || categorySelected == "Categoria")
                categorySelected = ""
            viewModel.insertPointOfInterest(name,description,coordinates,categorySelected)
        }){
            Text(stringResource(id = pt.isec.amov.tp.eguide.R.string.save))
            
        }

    }


}



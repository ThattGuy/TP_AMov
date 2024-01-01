package pt.isec.amov.tp.eguide.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import pt.isec.amov.tp.eguide.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseWhatToRegisterScreen(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(R.string.choose_what_to_register.toString()) }
    var coordinates by rememberSaveable {
        mutableStateOf("")
    }
    coordinates = navController.currentBackStackEntry?.arguments?.getString("coordinates") ?: ""

    val options = listOf(R.string.register_location.toString(), R.string.register_point_of_interest.toString()  )

    Column {

        Text(
            stringResource(id = R.string.choose_what_to_register),
        )
        options.forEach { option ->
            Button(
                onClick = {
                    navigateBasedOnSelection(option, navController, coordinates)
                },
                enabled = true
            ){
                Text(stringResource(id = option.toInt()) )
            }
        }
    }

}

private fun navigateBasedOnSelection(
    option: String,
    navController: NavController,
    coordinates: String = ""
) {
    when (option) {
        R.string.register_location.toString() -> navController.navigate("${Screens.REGISTER_LOCATION.route}/$coordinates")
        R.string.register_point_of_interest.toString() -> navController.navigate("${Screens.REGISTER_POINT_OF_INTEREST.route}/$coordinates")
    }
}

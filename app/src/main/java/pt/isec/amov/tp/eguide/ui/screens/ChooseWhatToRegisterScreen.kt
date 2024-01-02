package pt.isec.amov.tp.eguide.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.isec.amov.tp.eguide.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseWhatToRegisterScreen(navController: NavController) {
    var coordinates by rememberSaveable { mutableStateOf("") }
    coordinates = navController.currentBackStackEntry?.arguments?.getString("coordinates") ?: ""

    val options = listOf(R.string.register_location, R.string.register_point_of_interest)

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.choose_what_to_register),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        options.forEach { option ->
            StyledButton(
                text = stringResource(id = option),
                onClick = { navigateBasedOnSelection(option, navController, coordinates) }
            )
        }
    }
}

@Composable
fun StyledButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = text)
    }
}

private fun navigateBasedOnSelection(
    option: Int,
    navController: NavController,
    coordinates: String = ""
) {
    when (option) {
        R.string.register_location -> navController.navigate("${Screens.REGISTER_LOCATION.route}/$coordinates")
        R.string.register_point_of_interest -> navController.navigate("${Screens.REGISTER_POINT_OF_INTEREST.route}/$coordinates")
    }
}


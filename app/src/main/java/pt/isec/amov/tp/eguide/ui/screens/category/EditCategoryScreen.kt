package pt.isec.amov.tp.eguide.ui.screens.category

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel
import pt.isec.amov.tp.eguide.R
import pt.isec.amov.tp.eguide.ui.screens.Screens


@Composable
fun EditCategory(viewModel: LocationViewModel, navController: NavController) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        var categoryDescription by rememberSaveable { mutableStateOf("") }

        TextField(
            value = categoryDescription,
            onValueChange = { categoryDescription = it },
            label = {
                Text(
                    stringResource(id = R.string.category_description)
                )
            })

        Button(
            onClick = {
                viewModel.editCategory(categoryDescription)
                navController.navigate(Screens.LIST_CATEGORIES.route)
            },
            enabled = true
        ) {
            Text(stringResource(id = R.string.save))
        }


        Button(
            onClick = {
                viewModel.deleteCategory()
                navController.navigate(Screens.LIST_CATEGORIES.route)
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


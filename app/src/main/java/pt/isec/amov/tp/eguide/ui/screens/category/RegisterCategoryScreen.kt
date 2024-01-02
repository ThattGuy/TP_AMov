package pt.isec.amov.tp.eguide.ui.screens.category

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
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel
import pt.isec.amov.tp.eguide.R
import pt.isec.amov.tp.eguide.ui.screens.Screens


@Composable
fun RegisterCategory(viewModel: LocationViewModel, navController: NavController){
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally) {

        var categoryName by rememberSaveable { mutableStateOf("")}
        var categoryDescription by rememberSaveable { mutableStateOf("")}
        var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }

        // Validation check to enable or disable the save button
        val isFormValid = categoryName.isNotBlank() && categoryDescription.isNotBlank() && imageUri != null
        val pickImageLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            imageUri = uri
        }

        TextField(value = categoryName, onValueChange = { categoryName = it } , label = {Text(
            stringResource(id = R.string.category_name))})
        if (categoryName.isBlank()) {
            Text(stringResource(id = R.string.name_is_required), color = Color.Red)
        }
        TextField(value = categoryDescription, onValueChange = { categoryDescription = it } , label = {Text(
            stringResource(id = R.string.category_description))})
        if (categoryDescription.isBlank()) {
            Text(stringResource(id = R.string.description_is_required), color = Color.Red)
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
            viewModel.insertCategoryIntoDB(categoryName,categoryDescription)
            viewModel.insertCategoryImage(imageUri!!, categoryName)
            navController.navigate(Screens.LIST_CATEGORIES.route)
        }, enabled = isFormValid){
            Text(text = "Save")
        }

        if (!isFormValid) {
            Text(stringResource(id = R.string.fill_all_fields), color = Color.Red)
        }
    }
}

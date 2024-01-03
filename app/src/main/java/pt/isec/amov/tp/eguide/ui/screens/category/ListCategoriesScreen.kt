package pt.isec.amov.tp.eguide.ui.screens.category

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import pt.isec.amov.tp.eguide.R
import pt.isec.amov.tp.eguide.data.Category
import pt.isec.amov.tp.eguide.ui.screens.Screens
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel
import pt.isec.amov.tp.eguide.utils.firebase.FAuthUtil


@Composable
fun CategoryItem(category: Category, navController: NavController, viewModel: LocationViewModel) {
    val userId = FAuthUtil.currentUser?.uid.toString()
    val imageFile = remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(key1 = category.name) {
        viewModel.getCategoryImage(category.createdBy.toString(),category.name!!) { imageFile.value = it }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            imageFile.value?.let { uri ->
                SubcomposeAsyncImage(
                    model = uri,
                    loading = { CircularProgressIndicator() },
                    contentDescription = stringResource(id = R.string.category_image),
                    modifier = Modifier.size(100.dp) // Fixed size for the image
                )
            }

            Column(modifier = Modifier
                .padding(8.dp)
                .weight(1f)
            ) {
                Text(
                    text = category.name ?: stringResource(id = R.string.no_name),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = category.description ?: stringResource(id = R.string.no_description),
                )

                Column(horizontalAlignment = Alignment.End) {
                    if (userId == category.createdBy) {
                        SquareButton(text = stringResource(id = R.string.edit_category)) {
                            viewModel.categoryToEdit = category.name
                            navController.navigate(Screens.EDIT_CATEGORY.route)
                        }
                    }
                    if (userId != category.createdBy && !category.isApproved!! && !category.approvedByUsers?.contains(userId)!!) {
                        SquareButton(text = stringResource(id = R.string.approve)) {
                            viewModel.approveCategory(category, userId)
                            navController.navigate(Screens.LIST_CATEGORIES.route)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SquareButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = text)
    }
}


@Composable
fun ListCategories(viewModel: LocationViewModel, navController: NavController) {
    val list = viewModel.categories.observeAsState(initial = listOf())

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate(Screens.REGISTER_CATEGORY.route) }) {
            Text(text = stringResource(id = R.string.register_category))
        }
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(list.value) { category ->
                CategoryItem(category = category, navController = navController, viewModel = viewModel)
            }
        }
    }
}

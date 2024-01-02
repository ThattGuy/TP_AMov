package pt.isec.amov.tp.eguide.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import pt.isec.amov.tp.eguide.R
import pt.isec.amov.tp.eguide.data.Category
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel
import pt.isec.amov.tp.eguide.utils.firebase.FAuthUtil


@Composable
fun CategoryItem(category: Category,navController: NavController,viewModel: LocationViewModel) {
    val userId = FAuthUtil.currentUser?.uid.toString()
    val imageFile = remember {
        mutableStateOf<Uri?>(null)
    }

    LaunchedEffect(key1 = category.name) {
        viewModel.getCategoryImage(category.name!!) { imageFile.value = it }
    }
    Column(modifier = Modifier.padding(16.dp)) {
        Row {
            if(userId == category.createdBy)
            {
                Button(onClick = {
                    //TODO
                }) {
                    Text(text = stringResource(id = R.string.edit_category))
                }
            }
            Button(onClick = { /*TODO*/ }) {
                Text(
                    text = category.name
                        ?: stringResource(id = R.string.no_name)
                )
            }

            imageFile.value?.let { uri ->
                SubcomposeAsyncImage(
                    model = uri,
                    loading = {
                        CircularProgressIndicator()
                    },
                    contentDescription = stringResource(id = R.string.select_image)
                )
            }

            if(userId != category.createdBy && category.isApproved == false && !category.approvedByUsers!!.contains(userId))
            {
                Button(onClick = {
                    viewModel.approveCategory(category, userId)
                    navController.navigate(Screens.LIST_CATEGORIES.route)
                },
                    modifier = Modifier
                        .padding(5.dp)
                ) {
                    Text(text = stringResource(id = R.string.approve))
                }
            }
        }
    }
}


@Composable
fun ListCategories(viewModel : LocationViewModel,  navController: NavController)
{
    val list  = viewModel.categories.observeAsState(initial = listOf())


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
                CategoryItem(category = category,navController = navController,viewModel = viewModel)
            }
        }
    }
}
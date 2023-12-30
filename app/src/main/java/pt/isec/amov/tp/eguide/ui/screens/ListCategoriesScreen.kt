package pt.isec.amov.tp.eguide.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.isec.amov.tp.eguide.data.Category
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel


@Composable
fun CategoryItem(category: Category) {
    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { /*TODO*/ }) {
            Text(text = category.name ?: stringResource(id = pt.isec.amov.tp.eguide.R.string.no_name))
        }
    }
}


@Composable
fun ListCategories(viewModel : LocationViewModel,  navController: NavController)
{
    val list  = viewModel.getCategoriesList()

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Button(onClick = { navController.navigate(Screens.REGISTER_CATEGORY.route) }) {
            Text(text = stringResource(id = pt.isec.amov.tp.eguide.R.string.register_category))
        }
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            items(list) { category ->
                CategoryItem(category = category)
            }
        }
    }
}
package pt.isec.amov.tp.eguide.ui.screens


import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import pt.isec.amov.tp.eguide.R
import pt.isec.amov.tp.eguide.data.Location
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel
import pt.isec.amov.tp.eguide.utils.firebase.FAuthUtil


@Composable
fun LocationItem(location: Location, viewModel: LocationViewModel, navController: NavController) {
    val userId = FAuthUtil.currentUser?.uid.toString()
    val imageFile = remember {
        mutableStateOf<Uri?>(null)
    }

    LaunchedEffect(key1 = location.name) {
        viewModel.getLocationImage(location.name!!) { imageFile.value = it }
    }
    Column(modifier = Modifier.padding(16.dp)) {
        Row {

            if (userId == location.createdBy) {
                Button(onClick = {
                    //TODO
                }) {
                    Text(text = stringResource(id = R.string.edit_location))
                }
            }

            Button(onClick = {
                navController.navigate(Screens.LIST_POINTS_OF_INTEREST.route)
            }) {
                Text(
                    text = location.name
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
            if (userId != location.createdBy && location.isApproved == false && !location.approvedByUsers!!.contains(
                    userId
                )
            ) {
                Button(
                    onClick = {
                        viewModel.approveLocation(
                            location,
                            FAuthUtil.currentUser?.uid.toString()
                        )
                        navController.navigate(Screens.LIST_LOCATIONS.route)
                    },
                    modifier = Modifier
                        .background(Color.Blue)
                        .padding(5.dp)
                ) {
                    Text(text = stringResource(id = R.string.approve))
                }
            }
        }


    }
}

@Composable
fun ListLocations(viewModel: LocationViewModel, navController: NavController) {

    val lista = viewModel.locations.observeAsState(initial = listOf())

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (lista.value.isNotEmpty())
            Text(text = stringResource(id = R.string.no_locations))
        Button(onClick = { navController.navigate(Screens.REGISTER_LOCATION.route) }) {
            Text(text = "Registar local")
        }
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(lista.value) { location ->
                LocationItem(location = location, viewModel, navController)
            }
        }
    }

}

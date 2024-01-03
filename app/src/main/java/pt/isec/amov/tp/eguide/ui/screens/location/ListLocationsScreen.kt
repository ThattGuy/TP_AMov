package pt.isec.amov.tp.eguide.ui.screens.location

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
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import pt.isec.amov.tp.eguide.R
import pt.isec.amov.tp.eguide.data.Location
import pt.isec.amov.tp.eguide.ui.screens.Screens
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel
import pt.isec.amov.tp.eguide.utils.firebase.FAuthUtil


@Composable
fun LocationItem(location: Location, viewModel: LocationViewModel, navController: NavController) {
    val userId = FAuthUtil.currentUser?.uid.toString()
    val imageFile = remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(key1 = location.name) {
        viewModel.getLocationImage(location.createdBy.toString(),location.name!!) { imageFile.value = it }
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
                    contentDescription = stringResource(id = R.string.location_image),
                    modifier = Modifier.size(100.dp) // Fixed size for the image
                )
            }

            Column(modifier = Modifier
                .padding(8.dp)
                .weight(1f)
            ) {
                Text(
                    text = location.name ?: stringResource(id = R.string.no_name),
                    fontWeight = FontWeight.Bold
                )
                // Add more details here if needed

                Column(horizontalAlignment = Alignment.End) {
                    if (userId == location.createdBy) {
                        SquareButton(text = stringResource(id = R.string.edit_location)) {
                            viewModel.locationToEdit = location.name.toString()
                            navController.navigate(Screens.EDIT_LOCATION.route)
                        }
                    }
                    if (userId != location.createdBy && !location.isApproved!! && !location.approvedByUsers?.contains(userId)!!) {
                        SquareButton(text = stringResource(id = R.string.approve), backgroundColor = Color.Blue) {
                            viewModel.approveLocation(location, userId)
                            navController.navigate(Screens.LIST_LOCATIONS.route)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SquareButton(text: String, backgroundColor: Color = Color.Unspecified, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor),
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = text)
    }
}

@Composable
fun ListLocations(viewModel: LocationViewModel, navController: NavController) {
    val lista = viewModel.locations.observeAsState(initial = listOf())

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (lista.value.isEmpty())
            Text(text = stringResource(id = R.string.no_locations))

        Button(onClick = { navController.navigate(Screens.REGISTER_LOCATION.route) }) {
            Text(text = stringResource(id = R.string.register_location))
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
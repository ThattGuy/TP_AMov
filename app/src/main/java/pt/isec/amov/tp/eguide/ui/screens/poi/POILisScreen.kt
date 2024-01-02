package pt.isec.amov.tp.eguide.ui.screens.poi

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import pt.isec.amov.tp.eguide.R
import pt.isec.amov.tp.eguide.data.PointOfInterest
import pt.isec.amov.tp.eguide.ui.screens.Screens
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel
import pt.isec.amov.tp.eguide.utils.firebase.FAuthUtil


@Composable
fun PointOfInterestItem(
    pointOfInterest: PointOfInterest, navController: NavController,
    viewModel: LocationViewModel
) {
    val userId = FAuthUtil.currentUser?.uid.toString()
    val imageFile = remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(key1 = pointOfInterest.name) {
        viewModel.getPOIImage(pointOfInterest.name!!) { imageFile.value = it }
    }

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            imageFile.value?.let { uri ->
                SubcomposeAsyncImage(
                    model = uri,
                    loading = { CircularProgressIndicator() },
                    contentDescription = stringResource(id = R.string.poi_image),
                    modifier = Modifier.size(100.dp) // Fixed size for the image
                )
            }

            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = pointOfInterest.name ?: stringResource(id = R.string.no_name))

                Row {
                    if (userId == pointOfInterest.createdBy) {
                        SquareButton(text = stringResource(id = R.string.edit_point_of_interest)) {
                            //TODO
                        }
                    }
                    if (userId != pointOfInterest.createdBy && !pointOfInterest.isApproved!! && !pointOfInterest.approvedByUsers?.contains(userId)!!) {
                        SquareButton(text = stringResource(id = R.string.approve)) {
                            viewModel.approvePOI(pointOfInterest, userId)
                            navController.navigate(Screens.LIST_POINTS_OF_INTEREST.route)
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
fun ListPointsOfInterest(
    modifier: Modifier = Modifier,
    viewModel: LocationViewModel,
    navController: NavController
) {
    val listaTetse = viewModel.pois.observeAsState()
    /* for(i in 1..100)
     {
         listaTetse.add(PointOfInterest("${i}º Point"))
     }

     */
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Button(onClick = { navController.navigate(Screens.REGISTER_POINT_OF_INTEREST.route) }) {
            Text(text = stringResource(id = R.string.register_point_of_interest))
        }
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            items(listaTetse.value!!) { pointOfIterest ->
                PointOfInterestItem(
                    pointOfInterest = pointOfIterest,
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}

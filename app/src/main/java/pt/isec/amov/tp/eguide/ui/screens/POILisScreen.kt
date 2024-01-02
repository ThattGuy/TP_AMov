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
import pt.isec.amov.tp.eguide.data.PointOfInterest
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel
import pt.isec.amov.tp.eguide.utils.firebase.FAuthUtil


@Composable
fun PointOfInterestItem(
    pointOfInterest: PointOfInterest, navController: NavController,
    viewModel: LocationViewModel
) {
    val userId = FAuthUtil.currentUser?.uid.toString()
    val imageFile = remember {
        mutableStateOf<Uri?>(null)
    }

    LaunchedEffect(key1 = pointOfInterest.name) {
        viewModel.getPOIImage(pointOfInterest.name!!) { imageFile.value = it }
    }
    Column(modifier = Modifier.padding(16.dp)) {

        Row {

            if (userId == pointOfInterest.createdBy) {
                Button(onClick = {
                    //TODO
                }) {
                    Text(text = stringResource(id = R.string.edit_point_of_interest))
                }
            }

            Button(onClick = { /*TODO*/ }) {
                Text(
                    text = pointOfInterest.name
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
            if (userId != pointOfInterest.createdBy && pointOfInterest.isApproved == false && !pointOfInterest.approvedByUsers!!.contains(
                    userId
                )
            ) {
                Button(onClick = {
                    viewModel.approvePOI(pointOfInterest, userId)
                    navController.navigate(Screens.LIST_POINTS_OF_INTEREST.route)
                }) {
                    Text(text = stringResource(id = R.string.approve))
                }

            }
        }
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

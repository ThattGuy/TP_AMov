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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.isec.amov.tp.eguide.data.Location
import pt.isec.amov.tp.eguide.data.PointOfInterest
import pt.isec.amov.tp.eguide.ui.screens.uicomponents.Layout_Bars
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel


@Composable
fun PointOfInterestItem(pointOfInterest: PointOfInterest) {
    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { /*TODO*/ }) {
            Text(text = pointOfInterest.name ?: "Nome não disponível")

        }


    }
}


@Composable
fun ListPointsOfInterest(modifier: Modifier = Modifier, viewModel: LocationViewModel, navController: NavController) {
    val listaTetse  = ArrayList<PointOfInterest>()
    for(i in 1..100)
    {
        listaTetse.add(PointOfInterest("${i}º Point"))
    }

    LazyColumn(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        items(listaTetse) { pointOfIterest ->
            PointOfInterestItem(pointOfInterest = pointOfIterest)
        }
    }
}

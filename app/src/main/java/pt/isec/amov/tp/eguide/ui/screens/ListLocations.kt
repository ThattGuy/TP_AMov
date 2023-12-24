package pt.isec.amov.tp.eguide.ui.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.isec.amov.tp.eguide.data.Location
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel


@Composable
fun LocationItem(location: Location) {
    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { /*TODO*/ }) {
            Text(text = location.name ?: "Nome não disponível")
            Text(text = location.address ?: "Endereço não disponível")
        }


    }
}

@Composable
fun ListLocations(modifier: Modifier = Modifier, viewModel: LocationViewModel, navController: NavController) {
    val listaTetse  = ArrayList<Location>()
    for(i in 1..100)
    {
        listaTetse.add(Location("${i}º location","${i}º address"))
    }

    LazyColumn {
        items(listaTetse) { location ->
            LocationItem(location = location)
        }
    }
}

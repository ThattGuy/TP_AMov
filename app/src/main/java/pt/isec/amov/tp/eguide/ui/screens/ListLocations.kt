package pt.isec.amov.tp.eguide.ui.screens


import android.content.ContentValues
import android.util.Log
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pt.isec.amov.tp.eguide.data.Location
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel


@Composable
fun LocationItem(location: Location,viewModel: LocationViewModel,navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = {
            viewModel.locationSelected = location
            navController.navigate(Screens.LIST_POINTS_OF_INTEREST.route)
        }) {
            Text(text = location.name ?: "Nome não disponível")
            //Text(text = location.address ?: "Endereço não disponível")
        }


    }
}

@Composable
 fun ListLocations(viewModel: LocationViewModel,navController: NavController) {

       var lista = viewModel.getLocations()


   /* for(i in 1..100)
    {
        listaTetse.add(Location(
            "${i}º location",
            "${i}º address",
            document.data["Coordinates"].toString()
        ))
    }
    */



    Text(text = "Locais")
    if(lista.size == 0)
        Text(text = "Esta lista nao tem nada")
Column {


    Button(onClick = { navController.navigate(Screens.REGISTER_LOCATION.route) }) {
        Text(text = "Registar local")
    }

    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(lista) { location ->
            LocationItem(location = location, viewModel, navController)
        }
    }
}

}

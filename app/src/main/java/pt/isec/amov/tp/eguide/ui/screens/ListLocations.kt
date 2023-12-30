package pt.isec.amov.tp.eguide.ui.screens


import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pt.isec.amov.tp.eguide.data.Location
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel
import pt.isec.amov.tp.eguide.utils.firebase.FAuthUtil


@Composable
fun LocationItem(location: Location,viewModel: LocationViewModel,navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row {


            Button(onClick = {
                viewModel.locationSelected = location
                navController.navigate(Screens.LIST_POINTS_OF_INTEREST.route)
            }) {
                Text(text = location.name ?: "Nome não disponível")
                //Text(text = location.address ?: "Endereço não disponível")
            }
            if(FAuthUtil.currentUser?.uid != location.createdBy && location.isApproved == false)
            {
                Button(onClick = {
                 viewModel.userApprovesLocation(location, FAuthUtil.currentUser?.uid.toString())
                    navController.navigate(Screens.LIST_LOCATIONS.route)
                },
                modifier = Modifier.background(Color.Blue).padding(5.dp)
                ) {
                    Text(text = "Aprovar")
                }
            }
        }


    }
}

@Composable
 fun ListLocations(viewModel: LocationViewModel,navController: NavController) {

       val lista = viewModel.getLocations()


Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
if(lista.size == 0)
    Text(text = "A lista esta vazia")
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

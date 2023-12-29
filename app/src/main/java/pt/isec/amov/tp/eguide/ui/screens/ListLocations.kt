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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
 fun ListLocations(viewModel: LocationViewModel) {

       var lista = ArrayList<Location>()

        viewModel.getLocations(lista)
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

    LazyColumn(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        items(lista) { location ->
            LocationItem(location = location)
        }
    }

}

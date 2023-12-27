package pt.isec.amov.tp.eguide.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

@Composable
fun RegisterCategory(){
    Column(verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally) {

        var name by remember { mutableStateOf("")}
        TextField(value = name, onValueChange = { name = it } , label = {Text("Nome da categoria")})
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Save")
        }
    }
}

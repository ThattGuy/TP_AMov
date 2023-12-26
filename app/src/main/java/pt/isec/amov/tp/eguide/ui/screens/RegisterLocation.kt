package pt.isec.amov.tp.eguide.ui.screens

import android.content.Context
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import pt.isec.amov.tp.eguide.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateLocationScreen() {
    val viewModel : CreateLocationViewModel = remember { CreateLocationViewModel() }

    val context = LocalContext.current

    //val permissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            // Permissão concedida, faça o que precisar com a localização
            viewModel.getUserLocation(context)
        } else {
            // Permissão negada, trate conforme necessário
        }
    }

    Scaffold(


        content = {padding->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Campo de texto para descrição
                var description by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descrição") },
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { /* Ação ao pressionar 'Próximo' */ }
                    )
                )

                // Botão para adicionar foto
                var photoUri by remember { mutableStateOf("") }
                Button(
                    onClick = {
                        // Ação para adicionar foto
                        // Aqui você pode abrir a galeria/câmera para selecionar uma foto
                        // Depois de obter a URI da foto, atualize 'photoUri'
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Add, contentDescription = "Adicionar Foto")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Adicionar Foto")
                    }
                }

                // Mostrar a foto selecionada, se houver
                if (photoUri.isNotEmpty()) {
                    Image(
                        painter = painterResource(id = R.drawable.logo), 
                        contentDescription = "Foto"
                    )
                }

                // Botão para obter localização
                val locationEnabled = false
                //val locationEnabled = permissionState.hasPermission
                Button(
                    onClick = {
                        if (!locationEnabled) {
                            launcher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                        } else {
                            viewModel.getUserLocation(context)
                        }
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(if (locationEnabled) "Obter Localização" else "Permitir Localização")
                }

                // Mostrar a localização obtida, se houver
                val location = viewModel.location
                if (location != null) {
                    Text("Latitude: ${location.latitude}, Longitude: ${location.longitude}")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Salvar")
                }
            }
        }
    )
}

class CreateLocationViewModel : ViewModel() {
    var location: Location? by mutableStateOf(null)

    fun getUserLocation(context: Context) {
        // Lógica para obter a localização do usuário (pode usar LocationManager, FusedLocationProviderClient, etc.)
        // Atualize 'location' com a localização obtida
    }
}

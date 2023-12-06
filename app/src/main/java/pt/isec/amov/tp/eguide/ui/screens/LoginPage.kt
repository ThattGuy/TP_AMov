package pt.isec.amov.tp.eguide.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import pt.isec.amov.tp.eguide.R
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
//onLogin: (String, String) -> Unit
fun LoginScreen(modifier: Modifier = Modifier, viewModel: LocationViewModel, navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        /*Text(text = "Explores Guide",
            style = TextStyle(
            fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.5.sp
        ))

     */
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo da empresa",
            modifier = Modifier.size(150.dp) // Ajuste o tamanho conforme necessário
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /*onLogin(username, password)*/ }) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { navController.navigate("register") }) {

            Text("Register")

        }

    }


}
package com.example.tp_amov.ui.theme

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tp_amov.R

class RegisterScreen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TextBox(text: String, content: String) {
        Column(modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center)
        {
            TextField(
                modifier = Modifier
                    .background(Color.White),
                value = content,
                onValueChange = {content},
                label = { Text(text = text) },
                maxLines = 2,
                /*leadingIcon = {
                    IconButton(onClick = {  }) {
                        Icon(imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Profile Name Icon")
                    }
                },*/
                trailingIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.Check,
                            contentDescription = "Check Icon")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        Log.d("ImeAction", "Clicked")
                    }
                )
            )

        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun RegisterScreen(onRegister: (String, String, String, String, String) -> Unit){
        var name by remember { mutableStateOf("Type here...") }
        var username by remember { mutableStateOf("Type here...") }
        var email by remember { mutableStateOf("Type here...") }
        var password by remember { mutableStateOf("Type here...") }
        var cpassword by remember { mutableStateOf("Type here...") }

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            Image(painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Icon",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextBox("Name", name);
            Spacer(modifier = Modifier.height(16.dp))
            TextBox("Username", username);
            Spacer(modifier = Modifier.height(16.dp))
            TextBox("E-mail", email);
            Spacer(modifier = Modifier.height(16.dp))
            TextBox("New Password", password);
            Spacer(modifier = Modifier.height(16.dp))
            TextBox("Confirm New Password", cpassword);
            Spacer(modifier = Modifier.height(50.dp))

            Row (
                modifier = Modifier,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Button(onClick = { onRegister(name, username, email, password, cpassword) }) {
                    Text("Create Account")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { /*TODO*/ }) {
                    Text("Login")
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun RegisterScreenPreview() {
        RegisterScreen {
                name, username, email, password, cpassword ->
            Log.d("RegisterScreen",
                "Name: $name, " +
                        "Username: $username, " +
                        "E-mail: $email, " +
                        "Password: $password, " +
                        "Confirm Password: $cpassword")
            println("A usar println \n Name: $name, " +
                    "Username: $username, " +
                    "E-mail: $email, " +
                    "Password: $password, " +
                    "Confirm Password: $cpassword")
        }
    }

}
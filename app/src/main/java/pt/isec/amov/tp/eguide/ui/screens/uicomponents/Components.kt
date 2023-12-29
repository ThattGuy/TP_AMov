package pt.isec.amov.tp.eguide.ui.screens.uicomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import pt.isec.amov.tp.eguide.ui.screens.Screens
import pt.isec.amov.tp.eguide.ui.viewmodels.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Layout_Bars(
    showBottomBar: Boolean = true,
    viewModel: AuthViewModel,
    navController : NavHostController,
    content: @Composable (PaddingValues) -> Unit
){
    var showMenu: Boolean by remember { mutableStateOf(false) }
    val isAuthenticated = viewModel.isUserAuthenticated()

    fun navigateTo(route: String) {
        showMenu = false
        navController.navigate(route)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {"eGuide"},
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }

                    if(!isAuthenticated){
                        TextButton(onClick = { navController.navigate("login") }) {
                            Text("Login", color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }

                    if(isAuthenticated){
                        IconButton(onClick = { /* Handle Profile */ }) {
                            Icon(Icons.Filled.Person, contentDescription = "Profile")
                        }
                    }
                }
            )
        },
        bottomBar = {
            if(showBottomBar){
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "Bottom app bar",
                    )
                }
            }
        }
        ) {
            innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                content(innerPadding)

                OverlayMenu("Menu", showMenu, onDismiss = { showMenu = false }) {
                    // Logic for menu item click, e.g., navigate
                    navController.navigate("your_route_here")
                    }
                }
        }
}

@Composable
fun OverlayMenu(
    title: String,
    showMenu: Boolean,
    onDismiss: () -> Unit,
    onMenuItemClicked: () -> Unit
) {
    if (showMenu) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.5f))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Home", modifier = Modifier.clickable {
                    onMenuItemClicked()
                    onDismiss()
                })
                Spacer(modifier = Modifier.height(10.dp))
                Text("Destinations", modifier = Modifier.clickable {
                    onMenuItemClicked()
                    onDismiss()
                })
                // More menu items...
            }
        }
    }
}

@Composable
fun InitializationView( viewModel: AuthViewModel, navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(), // Fills the parent composable
        contentAlignment = Alignment.Center // Centers the content inside the Box
    ) {
        CircularProgressIndicator()
        LaunchedEffect(Unit) {
            // Perform initialization tasks
            delay(2000);
            // Example: Check if user is authenticated
            if (viewModel.isUserAuthenticated()) {
                navController.navigate(Screens.MAIN.route) {
                    popUpTo(Screens.Initialization.route) { inclusive = true }
                }
            } else {
                navController.navigate(Screens.Login.route) {
                    popUpTo(Screens.Initialization.route) { inclusive = true }
                }
            }
        }
    }
}



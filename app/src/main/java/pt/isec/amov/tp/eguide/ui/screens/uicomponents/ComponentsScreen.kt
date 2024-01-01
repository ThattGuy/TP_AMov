package pt.isec.amov.tp.eguide.ui.screens.uicomponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import pt.isec.amov.tp.eguide.R
import pt.isec.amov.tp.eguide.ui.screens.Screens
import pt.isec.amov.tp.eguide.ui.viewmodels.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Layout_Bars(
    viewModel: AuthViewModel,
    navController : NavHostController,
    content: @Composable (PaddingValues) -> Unit
){
    var showMenu: Boolean by remember { mutableStateOf(false) }
    var showMenuP: Boolean by remember { mutableStateOf(false) }
    var buttonPosition by remember { mutableStateOf(Offset.Zero) }

    val isAuthenticated = viewModel.isUserAuthenticated()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {R.string.app_name},
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }

                    if(!isAuthenticated){
                        TextButton(onClick = { navController.navigate(Screens.Login.route) }) {
                            Text("Login", color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }

                    if(isAuthenticated){
                        IconButton(onClick = { showMenuP = !showMenuP },
                            modifier = Modifier.onGloballyPositioned { coordinates ->
                                buttonPosition = coordinates.positionInRoot()
                            }
                            ) {
                            Icon(Icons.Filled.Person, contentDescription = "Profile Menu")
                        }
                    }
                }
            )
        },
        bottomBar = {
            if(showMenu){
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                ) {
                    Box ( modifier = Modifier
                        .fillMaxSize()
                        .clickable(onClick = { navController.navigate("creditos") }),
                        contentAlignment = Alignment.TopCenter,){
                        Text("Credits",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center)
                    }
                }
            }
        }
        ) {
            innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                content(innerPadding)

                OverlayMenu("MENU", showMenu, onDismiss = { showMenu = false }, navController) {
                    navController.navigate(Screens.MAIN.route)
                    navController.navigate(Screens.LIST_LOCATIONS.route)
                    navController.navigate(Screens.LIST_POINTS_OF_INTEREST.route)
                    navController.navigate(Screens.REGISTER_CATEGORY.route)
                    }

                AnimatedVisibility(
                    visible = showMenuP,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                    ) {
                    ProfileMenu("Profile", showMenuP, viewModel = viewModel,  onDismiss = { showMenuP = false }, navController = navController){
                        navController.navigate(Screens.PROFILE.route)
                        navController.navigate(Screens.PROFILE.route)
                        navController.navigate("My Contributions")
                        navController.navigate(Screens.CREDITS.route)
                    }
                }

            }
        }
}

@Composable
fun OverlayMenu(
    title: String,
    showMenu: Boolean,
    onDismiss: () -> Unit,
    navController: NavHostController,
    onMenuItemClicked: () -> Unit,
) {
    if (showMenu) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = 0.8f))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(10.dp))

                val buttonModifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)

                val buttonColors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )

                Button(onClick = {navController.navigate(Screens.MAIN.route){ launchSingleTop = true }},
                    modifier = buttonModifier,
                    colors = buttonColors
                ) {
                    Text(
                        stringResource(id = pt.isec.amov.tp.eguide.R.string.home),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                Button(onClick = {navController.navigate(Screens.LIST_LOCATIONS.route){ restoreState = true }},
                    modifier = buttonModifier,
                    colors = buttonColors
                ) {
                    Text( stringResource(id = pt.isec.amov.tp.eguide.R.string.locations),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                Button(onClick = {navController.navigate(Screens.LIST_POINTS_OF_INTEREST.route){ restoreState = true }},
                    modifier = buttonModifier,
                    colors = buttonColors
                ) {
                    Text( stringResource(id = pt.isec.amov.tp.eguide.R.string.points_of_interest),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                Button(onClick = {navController.navigate(Screens.LIST_CATEGORIES.route){ restoreState = true }},
                    modifier = buttonModifier,
                    colors = buttonColors
                ) {
                    Text( stringResource(id = pt.isec.amov.tp.eguide.R.string.categories),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
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
            delay(2000)
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

@Composable
fun ProfileMenu(
    title: String,
    showMenuP: Boolean,
    viewModel: AuthViewModel,
    navController : NavHostController,
    onDismiss: () -> Unit,
    onMenuItemClicked: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.5f))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.TopEnd
    ) {

        Column(
            modifier = Modifier
                .width(200.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(20.dp))
            Text("Edit User Info", Modifier.clickable {
                // Navigate to Edit User Info screen
                navController.navigate(Screens.PROFILE.route)
                onDismiss()
            })
            Spacer(Modifier.height(16.dp))
            Text("My Contributions", Modifier.clickable {
                // Navigate to My Contributions screen
                navController.navigate(Screens.CONTRIBUTIONS.route)
                onDismiss()
            })
            Spacer(Modifier.height(16.dp))
            Text("Credits", Modifier.clickable {
                // Navigate to Credits screen
                navController.navigate(Screens.CREDITS.route)
                onDismiss()
            })
            Spacer(Modifier.height(30.dp))
            Text("Log out", Modifier.clickable {
                viewModel.signOut()
                // Optionally navigate to the login screen
                navController.navigate(Screens.Login.route) {
                    popUpTo("mainRoute") { inclusive = true } // Adjust the route name as needed
                }
                onDismiss()
            })
        }
    }
}

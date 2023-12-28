package pt.isec.amov.tp.eguide.ui

import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pt.isec.amov.tp.eguide.ui.screens.CreateLocationScreen
import pt.isec.amov.tp.eguide.ui.screens.ListLocations
import pt.isec.amov.tp.eguide.ui.screens.MainScreen
import pt.isec.amov.tp.eguide.ui.screens.RegisterCategory
import pt.isec.amov.tp.eguide.ui.screens.auth.RegisterScreen
import pt.isec.amov.tp.eguide.ui.screens.Screens
import pt.isec.amov.tp.eguide.ui.screens.auth.LoginScreen
import pt.isec.amov.tp.eguide.ui.viewmodels.AuthViewModel
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModelFactory


@Composable
fun SetupNavGraph(navController: NavHostController, authViewModel: AuthViewModel, locationViewModel: LocationViewModel) {
    NavHost(
        navController = navController,
        //startDestination = Screens.Login.route
        startDestination = Screens.REGISTER_CATEGORY.route
    ) {
        composable(Screens.Login.route) {
            LoginScreen(viewModel = authViewModel, navController = navController){
                navController.navigate(Screens.MAIN.route)
            }
        }
        composable(Screens.REGISTER.route) {
            RegisterScreen(viewModel = authViewModel){
                navController.navigate(Screens.MAIN.route)
            }
        }
        composable(Screens.MAIN.route) {
            MainScreen(viewModel = locationViewModel)
        }
        composable(Screens.LIST_LOCATIONS.route){
            ListLocations(viewModel = locationViewModel, navController = navController)
        }
        composable(Screens.REGISTER_LOCATION.route){
            CreateLocationScreen()
        }
        composable(Screens.REGISTER_CATEGORY.route){
            RegisterCategory(locationViewModel)
        }
    }
}
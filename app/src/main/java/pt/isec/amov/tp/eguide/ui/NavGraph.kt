package pt.isec.amov.tp.eguide.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pt.isec.amov.tp.eguide.ui.screens.CreateLocationScreen
import pt.isec.amov.tp.eguide.ui.screens.Credits
import pt.isec.amov.tp.eguide.ui.screens.ListCategories
import pt.isec.amov.tp.eguide.ui.screens.ListLocations
import pt.isec.amov.tp.eguide.ui.screens.ListPointsOfInterest
import pt.isec.amov.tp.eguide.ui.screens.MainScreen
import pt.isec.amov.tp.eguide.ui.screens.MyContributions
import pt.isec.amov.tp.eguide.ui.screens.RegisterCategory
import pt.isec.amov.tp.eguide.ui.screens.RegisterPointOfInterest
import pt.isec.amov.tp.eguide.ui.screens.auth.RegisterScreen
import pt.isec.amov.tp.eguide.ui.screens.Screens
import pt.isec.amov.tp.eguide.ui.screens.auth.EditUserInfoScreen
import pt.isec.amov.tp.eguide.ui.screens.auth.LoginScreen
import pt.isec.amov.tp.eguide.ui.screens.uicomponents.InitializationView
import pt.isec.amov.tp.eguide.ui.screens.uicomponents.Layout_Bars
import pt.isec.amov.tp.eguide.ui.viewmodels.AuthViewModel
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel


@Composable
fun SetupNavGraph(navController: NavHostController,
                  authViewModel: AuthViewModel,
                  locationViewModel: LocationViewModel) {

    NavHost(
        navController = navController,
        startDestination = Screens.Initialization.route
        //startDestination = Screens.LIST_LOCATIONS.route

    ) {
        composable(Screens.Initialization.route){
            InitializationView(viewModel = authViewModel, navController = navController)
        }
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
            Layout_Bars(viewModel = authViewModel, navController = navController) {
                MainScreen(viewModel = locationViewModel)
            }
        }
        composable(Screens.LIST_LOCATIONS.route){
            //ListLocations(viewModel = locationViewModel, navController = navController)
            Layout_Bars(viewModel = authViewModel, navController = navController) {
                ListLocations(viewModel = locationViewModel, navController = navController)
            }
        }
        composable(Screens.REGISTER_LOCATION.route){
            Layout_Bars(viewModel = authViewModel, navController = navController) {
                CreateLocationScreen(locationViewModel)
            }
        }
        composable(Screens.REGISTER_CATEGORY.route){
            Layout_Bars(viewModel = authViewModel, navController = navController) {
                RegisterCategory(locationViewModel)
            }
        }
        composable(Screens.REGISTER_POINT_OF_INTEREST.route){
            Layout_Bars(viewModel = authViewModel, navController = navController) {
                RegisterPointOfInterest(navController = navController, viewModel = locationViewModel)
            }
        }
        composable(Screens.LIST_POINTS_OF_INTEREST.route) {
            Layout_Bars(viewModel = authViewModel, navController = navController) {
                ListPointsOfInterest(viewModel = locationViewModel, navController = navController)
            }
        }
        composable(Screens.LIST_CATEGORIES.route) {
            Layout_Bars(viewModel = authViewModel, navController = navController) {
                ListCategories(viewModel = locationViewModel, navController = navController)
            }
        }

        composable(Screens.PROFILE.route) {
            Layout_Bars(viewModel = authViewModel, navController = navController) {
                EditUserInfoScreen(viewModel = authViewModel, navController = navController)
            }
        }

        composable(Screens.CONTRIBUTIONS.route) {
            Layout_Bars(viewModel = authViewModel, navController = navController) {
                MyContributions()
            }
        }


        composable(Screens.Credits.route) {
            Layout_Bars(viewModel = authViewModel, navController = navController) {
                Credits()
            }
        }
    }
}
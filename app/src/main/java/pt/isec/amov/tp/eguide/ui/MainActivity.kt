package pt.isec.amov.tp.eguide.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.isec.amov.tp.eguide.ui.theme.TP_AmovTheme
import com.google.firebase.FirebaseApp
import pt.isec.amov.tp.eguide.EGuide
import pt.isec.amov.tp.eguide.ui.screens.LoginScreen
import pt.isec.amov.tp.eguide.ui.screens.MainScreen
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModelFactory


class MainActivity : ComponentActivity() {

    private val app by lazy { application as EGuide }
    private val viewModel: LocationViewModel by viewModels {
        LocationViewModelFactory(app.locationHandler)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lateinit var navController: NavHostController

        FirebaseApp.initializeApp(this)
        setContent {
            TP_AmovTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    navController = rememberNavController()
                    SetupNavGraph(navController = navController, viewModel = viewModel)

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TP_AmovTheme {
        Greeting("Android")
    }
}
package pt.isec.amov.tp.eguide.ui

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.isec.amov.tp.eguide.ui.theme.TP_AmovTheme
import com.google.firebase.FirebaseApp
import pt.isec.amov.tp.eguide.EGuide
import pt.isec.amov.tp.eguide.ui.screens.auth.LoginScreen
import pt.isec.amov.tp.eguide.ui.screens.MainScreen
import pt.isec.amov.tp.eguide.ui.viewmodels.AuthViewModel
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModelFactory


class MainActivity : ComponentActivity() {

    private val app by lazy { application as EGuide }
    private val locationViewModel: LocationViewModel by viewModels {
        LocationViewModelFactory(app.locationHandler)
    }
    private val authViewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lateinit var navController: NavHostController

        FirebaseApp.initializeApp(this)
        setContent {
            TP_AmovTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    navController = rememberNavController()
                    SetupNavGraph(navController = navController, authViewModel = authViewModel, locationViewModel = locationViewModel)
                }
            }
            intent.extras?.apply {
                for(k in keySet()) {
                    Log.i("MainActivity", "Extras: $k -> ${get(k)}")
                }
            }
        }
        verifyPermissions()
    }

    override fun onResume() {
        super.onResume()
        locationViewModel.startLocationUpdates()
    }

    private fun verifyPermissions(): Boolean {
        locationViewModel.coarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        locationViewModel.fineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            locationViewModel.backgroundLocationPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else
            locationViewModel.backgroundLocationPermission =
                locationViewModel.coarseLocationPermission || locationViewModel.fineLocationPermission

        if (!locationViewModel.coarseLocationPermission && !locationViewModel.fineLocationPermission) {
            basicPermissionsAuthorization.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            return false
        } else
            verifyBackgroundPermission()
        return true
    }

    private val basicPermissionsAuthorization = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        locationViewModel.coarseLocationPermission =
            results[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        locationViewModel.fineLocationPermission =
            results[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        locationViewModel.startLocationUpdates()
        verifyBackgroundPermission()
    }

    private fun verifyBackgroundPermission() {
        if (!(locationViewModel.coarseLocationPermission || locationViewModel.fineLocationPermission))
            return

        if (!locationViewModel.backgroundLocationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            ) {
                val dlg = AlertDialog.Builder(this)
                    .setTitle("Background Location")
                    .setMessage(
                        "This application needs your permission to use location while in the background.\n" +
                                "Please choose the correct option in the following screen" +
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                                    " (\"${packageManager.backgroundPermissionOptionLabel}\")."
                                else
                                    "."
                    )
                    .setPositiveButton("Ok") { _, _ ->
                        backgroundPermissionAuthorization.launch(
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        )
                    }
                    .create()
                dlg.show()
            }
        }
    }

    private val backgroundPermissionAuthorization = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        locationViewModel.backgroundLocationPermission = result
        Toast.makeText(this, "Background location enabled: $result", Toast.LENGTH_LONG).show()
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
package pt.isec.amov.tp.eguide.ui.screens

import android.preference.PreferenceManager
import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay
import pt.isec.amov.tp.eguide.ui.viewmodels.Coordinates
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier, viewModel: LocationViewModel, navController: NavHostController) {
    var autoEnabled by remember { mutableStateOf(false) }
    val location = viewModel.currentLocation.observeAsState()
    val nearbyPois = viewModel.nearbyPOIs.observeAsState()

    var geoPoint by remember {
        mutableStateOf(
            GeoPoint(
                location.value?.latitude ?: 0.0, location.value?.longitude ?: 0.0
            )
        )
    }

    val context = LocalContext.current

    DisposableEffect(Unit) {
        Configuration.getInstance().load(
            context,
            PreferenceManager.getDefaultSharedPreferences(context)
        )
        onDispose { }
    }

    if (autoEnabled)
        LaunchedEffect(key1 = location.value) {
            geoPoint = GeoPoint(
                location.value?.latitude ?: 0.0, location.value?.longitude ?: 0.0
            )
        }

    fun navigateToCreatePOIScreen(selectedGeoPoint: GeoPoint) {
        val locationString = "${selectedGeoPoint.latitude},${selectedGeoPoint.longitude}"
        navController.navigate("${Screens.CHOOSEWHATTOREGISTER.route}/$locationString")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Lat: ${location.value?.latitude ?: "--"}")
            Switch(checked = autoEnabled, onCheckedChange = { autoEnabled = it })
            Text(text = "Lon: ${location.value?.longitude ?: "--"}")
        }
        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .clipToBounds()
                .background(Color(255, 240, 128)),
        ) {
            AndroidView(
                factory = { context ->
                    MapView(context).apply {
                        setTileSource(TileSourceFactory.MAPNIK)
                        setMultiTouchControls(true)
                        controller.setCenter(geoPoint)
                        controller.setZoom(18.0)

                        // Add markers for nearby POIs
                        nearbyPois.value?.forEach { poi ->
                            val latLong = poi.coordinates!!.split(",").map { it.toDouble() }
                            val lat = latLong[0]
                            val lon = latLong[1]
                            val poiGeoPoint = GeoPoint(lat, lon)
                            overlays.add(
                                Marker(this).apply {
                                    position = poiGeoPoint
                                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                    title = poi.name
                                }
                            )
                        }

                        // Add touch listener for location selection
                        overlayManager.add(object : Overlay() {
                            override fun onSingleTapConfirmed(e: MotionEvent?, mapView: MapView?): Boolean {
                                e?.let {
                                    val selectedGeoPoint = mapView?.projection?.fromPixels(e.x.toInt(), e.y.toInt()) as GeoPoint
                                    navigateToCreatePOIScreen(selectedGeoPoint)
                                }
                                return true
                            }
                        })
                    }
                },
                update = { view ->
                    view.controller.setCenter(geoPoint)
                }
            )
        }

        Spacer(Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(nearbyPois.value ?: listOf()) { poi ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(128, 192, 255)),
                    onClick = {
                        val latLong = poi.coordinates!!.split(",").map { it.toDouble() }
                        val lat = latLong[0]
                        val lon = latLong[1]
                        val poiGeoPoint = GeoPoint(lat, lon)
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        poi.name?.let { Text(text = it, fontSize = 20.sp) }
                        val latLong = poi.coordinates!!.split(",").map { it.toDouble() }
                        val lat = latLong[0]
                        val lon = latLong[1]
                        val poiGeoPoint = GeoPoint(lat, lon)
                        Text(text = "${lat}, ${lon}", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}
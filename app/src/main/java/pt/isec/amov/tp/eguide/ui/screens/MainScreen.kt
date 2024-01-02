package pt.isec.amov.tp.eguide.ui.screens

import android.content.Context
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel

@Composable
fun MainScreen(modifier: Modifier = Modifier, viewModel: LocationViewModel, navController: NavHostController) {
    var autoEnabled by remember { mutableStateOf(false) }
    val location = viewModel.currentLocation.observeAsState()
    val orderedPois = viewModel.orderedPois.observeAsState(initial = listOf())

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
            context.getSharedPreferences("${context.packageName}_preferences", Context.MODE_PRIVATE)
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
                        viewModel.mapBoundingBox.value = boundingBox
                    }
                },
                update = { view ->
                    viewModel.mapBoundingBox.value = view.boundingBox
                    view.overlays.clear()

                    val poiMarkers = viewModel.refreshVisiblePois()

                    // Add markers for nearby POIs
                    poiMarkers.forEach { poi ->
                        val poiGeoPoint = poi.toGeoPoint()
                        view.overlays.add(
                            Marker(view).apply {
                                position = poiGeoPoint
                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                title = poi.name
                            }
                        )
                    }

                   view.overlayManager.add(object : Overlay() {
                        override fun onSingleTapConfirmed(e: MotionEvent?, mapView: MapView?): Boolean {
                            e?.let {
                                val selectedGeoPoint = mapView?.projection?.fromPixels(e.x.toInt(), e.y.toInt()) as GeoPoint
                                navigateToCreatePOIScreen(selectedGeoPoint)
                            }
                            return true
                        }
                    })
                }
            )
        }

        Spacer(Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(orderedPois.value ?: listOf()) { poi ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(128, 192, 255)),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        poi.name?.let { Text(text = it, fontSize = 20.sp) }
                        val poiGeoPoint = poi.toGeoPoint()

                        Text(text = "${poiGeoPoint.latitude}, ${poiGeoPoint.longitude}", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}
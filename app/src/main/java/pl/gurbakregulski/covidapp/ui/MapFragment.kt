package pl.gurbakregulski.covidapp.ui

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.MapView
import com.google.android.libraries.maps.OnMapReadyCallback
import com.google.android.libraries.maps.model.MapStyleOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import pl.gurbakregulski.covidapp.R
import pl.gurbakregulski.covidapp.databinding.MapFragmentBinding
import pl.gurbakregulski.covidapp.ui.MainActivity.Companion.LOCATION_PERMISSION_REQUEST_CODE
import pl.gurbakregulski.covidapp.viewmodel.MainActivityViewModel
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Suppress("EXPERIMENTAL_API_USAGE")
class MapFragment : Fragment() {

    private lateinit var binding: MapFragmentBinding
    private val viewModel: MainActivityViewModel by activityViewModels()
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MapFragmentBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = activity
            viewModel = this@MapFragment.viewModel
        }
        binding.map.onCreate(savedInstanceState)

        lifecycleScope.launchWhenStarted {
            googleMap = getGoogleMap(binding.map)
            setMapStyle(googleMap)
            setupMyLocation()
            viewModel.statsMarkers.observe(requireActivity(), Observer {
                it.forEach { marker ->
                    googleMap.addMarker(marker)
                }
            })
        }

        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.any { it == PERMISSION_GRANTED }) {
            onPermissionGranted()
        }
    }

    @SuppressLint("MissingPermission")
    private fun setupMyLocation() {
        if (isPermissionsGranted()) {
            onPermissionGranted()
        } else {
            requestPermission()
        }
    }

    private fun isPermissionsGranted(): Boolean {
        return checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) == PERMISSION_GRANTED ||
                checkSelfPermission(requireContext(), ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED
    }

    @RequiresPermission(anyOf = [ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION])
    private fun onPermissionGranted() {
        googleMap.isMyLocationEnabled = true
        viewModel.updateLocation {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 4.5f))
        }
    }

    private fun requestPermission() {
        requestPermissions(
            arrayOf(
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun setMapStyle(googleMap: GoogleMap) {
        googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(),
                R.raw.map_style
            )
        )
    }

    @ExperimentalCoroutinesApi
    suspend fun awaitCallback(block: (OnMapReadyCallback) -> Unit): GoogleMap =
        suspendCoroutine { cont ->
            block(OnMapReadyCallback { googleMap -> cont.resume(googleMap) })
        }

    @ExperimentalCoroutinesApi
    private suspend fun getGoogleMap(mapView: MapView): GoogleMap =
        withContext(Dispatchers.Main) {
            return@withContext awaitCallback { block ->
                mapView.getMapAsync(block)
            }
        }

    override fun onPause() {
        binding.map.onPause()
        super.onPause()
    }

    override fun onResume() {
        binding.map.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        binding.map.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        binding.map.onLowMemory()
        super.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        binding.map.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

}

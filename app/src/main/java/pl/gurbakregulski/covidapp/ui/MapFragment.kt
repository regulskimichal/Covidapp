package pl.gurbakregulski.covidapp.ui

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.gurbakregulski.covidapp.databinding.MapFragmentBinding
import pl.gurbakregulski.covidapp.viewmodel.MainActivityViewModel

class MapFragment : Fragment() {

    private lateinit var binding: MapFragmentBinding
    private val viewModel: MainActivityViewModel by sharedViewModel()
    private val geocoder: Geocoder by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MapFragmentBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = activity
            viewModel = this@MapFragment.viewModel
        }
        binding.map.apply {
            onCreate(savedInstanceState)
            getMapAsync { googleMap ->
                googleMap.setOnInfoWindowClickListener {
                    Toast.makeText(
                        requireContext(),
                        "Clicked: ${it.toString()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    true
                }
                val result = geocoder.getFromLocationName("Poland", 1)
                val poland = LatLng(result[0].latitude, result[0].longitude)
                googleMap.addMarker(MarkerOptions().position(poland).snippet("dsadasdasad").visible(true))
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(poland))
            }
        }
        return binding.root
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

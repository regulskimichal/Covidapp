package pl.gurbakregulski.covidapp.viewmodel

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.location.Geocoder
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.gurbakregulski.covidapp.model.Stats
import java.io.IOException
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val covid19Service: Covid19Service,
    private val locationService: LocationService,
    private val geocoder: Geocoder
) : ViewModel() {

    private val countryFilter: MutableLiveData<String> = MutableLiveData("")
    private val stats = MutableLiveData<List<Stats>>()
    val filteredStats: LiveData<List<Stats>> = Transformations.switchMap(countryFilter) { query ->
        when {
            query.isNullOrBlank() -> stats
            else -> {
                Transformations.switchMap(stats) { _stats ->
                    val filtered = _stats.filter { stat ->
                        stat.country.lowercase(Locale.getDefault()).contains(query)
                    }
                    MutableLiveData(filtered)
                }
            }
        }
    }
    val statsMarkers = MutableLiveData<List<MarkerOptions>>()

    fun updateData(onError: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                //throw RuntimeException()
                val fetchedStats = covid19Service.getSummary().asList()
                stats.value = fetchedStats
                statsMarkers.value = fetchCountriesCoordinates(fetchedStats)
            } catch (e: Exception) {
                Log.e(TAG, e.message, e)
                onError(e)
            }
        }
    }

    fun setFilterStats(countryName: String?) {
        countryFilter.value = countryName?.lowercase(Locale.getDefault())
    }

    @RequiresPermission(anyOf = [ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION])
    fun updateLocation(block: (LatLng) -> Unit) {
        viewModelScope.launch {
            val currentLocation = locationService.getCurrentLocation()
            if (currentLocation != null) {
                block(LatLng(currentLocation.latitude, currentLocation.longitude))
            }
        }
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    private suspend fun fetchCountriesCoordinates(stats: List<Stats>): List<MarkerOptions> {
        return stats.asFlow()
            .flatMapMerge { _stats ->
                flow {
                    val address = try {
                        withContext(IO) {
                            geocoder.getFromLocationName(_stats.country, 1).firstOrNull()
                        }
                    } catch (e: IOException) {
                        null
                    }

                    if (address != null) {
                        val marker = MarkerOptions()
                            .position(LatLng(address.latitude, address.longitude))
                            .title("${_stats.country} total confirmed: ${_stats.totalConfirmed}")
                            .visible(true)
                        emit(marker)
                    }
                }
            }
            .toList()
    }

    companion object {
        val TAG = MainActivityViewModel::class.simpleName
    }

}

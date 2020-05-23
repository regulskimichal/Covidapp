package pl.gurbakregulski.covidapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.gurbakregulski.covidapp.model.Covid19Service
import pl.gurbakregulski.covidapp.model.Stats
import java.util.*

class MainActivityViewModel(
    private val covid19Service: Covid19Service
) : ViewModel() {

    private val countryFilter: MutableLiveData<String> = MutableLiveData("")
    private val stats = MutableLiveData<List<Stats>>()
    val filteredStats: LiveData<List<Stats>> = Transformations.switchMap(countryFilter) { query ->
        when {
            query.isNullOrBlank() -> stats
            else -> {
                Transformations.switchMap(stats) { _stats ->
                    val filtered = _stats.filter { stat ->
                        stat.country.toLowerCase(Locale.getDefault()).contains(query)
                    }
                    MutableLiveData(filtered)
                }
            }
        }
    }

    fun updateData(onError: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                throw RuntimeException()
                stats.value = covid19Service.getSummary().asList()
            } catch (e: Exception) {
                Log.e(TAG, e.message, e)
                onError(e)
            }
        }
    }

    fun filterStats(countryName: String?) {
        countryFilter.value = countryName?.toLowerCase(Locale.getDefault())
    }

    companion object {
        val TAG = MainActivityViewModel::class.simpleName
    }

}

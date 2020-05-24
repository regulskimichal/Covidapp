package pl.gurbakregulski.covidapp.viewmodel

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.location.Location
import android.util.Log
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationService(
    private val locationClient: FusedLocationProviderClient
) {

    @RequiresPermission(anyOf = [ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION])
    suspend fun getCurrentLocation(): Location? {
        try {
            return locationClient.getLastLocationAsync()
        } catch (ex: Exception) {
            Log.wtf(TAG, ex.message, ex)
        }
        return null
    }

    @RequiresPermission(anyOf = [ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION])
    private suspend fun FusedLocationProviderClient.getLastLocationAsync() =
        suspendCancellableCoroutine<Location?> { continuation ->
            lastLocation.addOnSuccessListener {
                continuation.resume(it)
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }

    companion object {
        private val TAG = LocationService::class.simpleName
    }

}

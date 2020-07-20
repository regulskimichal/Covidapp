package pl.gurbakregulski.covidapp

import com.blongho.country_data.World
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application : android.app.Application() {

    override fun onCreate() {
        super.onCreate()
        World.init(this)
    }

}

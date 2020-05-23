package pl.gurbakregulski.covidapp

import android.location.Geocoder
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import pl.gurbakregulski.covidapp.model.Covid19Repository
import pl.gurbakregulski.covidapp.model.Covid19Service
import pl.gurbakregulski.covidapp.viewmodel.MainActivityViewModel
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.create

class Application : android.app.Application() {

    private val module = module {
        single { jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }
        single { JacksonConverterFactory.create(get()) }
        single {
            Retrofit
                .Builder()
                .baseUrl(Covid19Repository.BASE_URL)
                .addConverterFactory(get<JacksonConverterFactory>())
                .build()
        }
        single { get<Retrofit>().create<Covid19Repository>() }
        single { Covid19Service(get()) }
        single { Geocoder(androidApplication()) }
        viewModel {
            MainActivityViewModel(
                get()
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Application)
            fragmentFactory()
            modules(module)
        }
    }

}

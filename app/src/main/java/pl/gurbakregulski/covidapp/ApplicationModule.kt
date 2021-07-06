package pl.gurbakregulski.covidapp

import android.app.Application
import android.location.Geocoder
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.gurbakregulski.covidapp.model.Covid19Repository
import pl.gurbakregulski.covidapp.viewmodel.Covid19Service
import pl.gurbakregulski.covidapp.viewmodel.LocationService
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun locationClient(application: Application): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    @Provides
    @Singleton
    fun locationService(locationClient: FusedLocationProviderClient): LocationService =
        LocationService(locationClient)


    @Provides
    @Singleton
    fun objectMapper(): ObjectMapper = jacksonObjectMapper()
        .registerModule(JavaTimeModule())
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

    @Provides
    @Singleton
    fun jacksonConverter(objectMapper: ObjectMapper): JacksonConverterFactory =
        JacksonConverterFactory.create(objectMapper)

    @Provides
    @Singleton
    fun retrofit(jacksonConverterFactory: JacksonConverterFactory): Retrofit = Retrofit
        .Builder()
        .baseUrl(Covid19Repository.BASE_URL)
        .addConverterFactory(jacksonConverterFactory)
        .build()

    @Provides
    @Singleton
    fun covid19Repository(retrofit: Retrofit): Covid19Repository = retrofit.create()

    @Provides
    @Singleton
    fun covid19Service(covid19Repository: Covid19Repository): Covid19Service =
        Covid19Service(covid19Repository)

    @Provides
    @Singleton
    fun geocoder(application: Application): Geocoder = Geocoder(application)

}

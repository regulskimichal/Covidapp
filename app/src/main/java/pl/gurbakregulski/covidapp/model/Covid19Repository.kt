package pl.gurbakregulski.covidapp.model

import retrofit2.http.GET

interface Covid19Repository {

    @GET("/summary")
    suspend fun getSummary(): SummaryStats

    companion object {
        const val BASE_URL = "https://api.covid19api.com/"
    }

}

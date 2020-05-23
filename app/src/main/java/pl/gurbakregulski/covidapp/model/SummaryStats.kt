package pl.gurbakregulski.covidapp.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class SummaryStats(
    @JsonProperty("Global") val global: GlobalStats,
    @JsonProperty("Countries") val countries: List<CountryStats>,
    @JsonProperty("Date") val date: Instant
) {
    fun asList() = listOf(global) + countries
}

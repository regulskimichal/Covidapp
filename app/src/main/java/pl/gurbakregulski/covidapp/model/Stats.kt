package pl.gurbakregulski.covidapp.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

sealed class Stats(
    open val country: String,
    open val newConfirmed: Long,
    open val totalConfirmed: Long,
    open val newDeaths: Long,
    open val totalDeaths: Long,
    open val newRecovered: Long,
    open val totalRecovered: Long
)

data class CountryStats(
    @JsonProperty("Country") override val country: String,
    @JsonProperty("CountryCode") val countryCode: String,
    @JsonProperty("Slug") val countryId: String,
    @JsonProperty("NewConfirmed") override val newConfirmed: Long,
    @JsonProperty("TotalConfirmed") override val totalConfirmed: Long,
    @JsonProperty("NewDeaths") override val newDeaths: Long,
    @JsonProperty("TotalDeaths") override val totalDeaths: Long,
    @JsonProperty("NewRecovered") override val newRecovered: Long,
    @JsonProperty("TotalRecovered") override val totalRecovered: Long,
    @JsonProperty("Date") val date: Instant
) : Stats(country, newConfirmed, totalConfirmed, newDeaths, totalDeaths, newRecovered, totalRecovered)

data class GlobalStats(
    @JsonProperty("NewConfirmed") override val newConfirmed: Long,
    @JsonProperty("TotalConfirmed") override val totalConfirmed: Long,
    @JsonProperty("NewDeaths") override val newDeaths: Long,
    @JsonProperty("TotalDeaths") override val totalDeaths: Long,
    @JsonProperty("NewRecovered") override val newRecovered: Long,
    @JsonProperty("TotalRecovered") override val totalRecovered: Long
) : Stats("Global", newConfirmed, totalConfirmed, newDeaths, totalDeaths, newRecovered, totalRecovered)

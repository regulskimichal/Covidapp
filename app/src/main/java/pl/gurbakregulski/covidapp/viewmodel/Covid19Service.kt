package pl.gurbakregulski.covidapp.viewmodel

import pl.gurbakregulski.covidapp.model.Covid19Repository
import pl.gurbakregulski.covidapp.model.SummaryStats

class Covid19Service(
    private val covid19Repository: Covid19Repository
) {

    suspend fun getSummary(): SummaryStats {
        return covid19Repository.getSummary()
    }

}

package pl.gurbakregulski.covidapp.model

class Covid19Service(
    private val covid19Repository: Covid19Repository
) {

    suspend fun getSummary(): SummaryStats {
        return covid19Repository.getSummary()
    }

}

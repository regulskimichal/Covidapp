package pl.gurbakregulski.covidapp.ui

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.blongho.country_data.World
import pl.gurbakregulski.covidapp.R
import pl.gurbakregulski.covidapp.model.CountryStats
import pl.gurbakregulski.covidapp.model.GlobalStats
import pl.gurbakregulski.covidapp.model.Stats

class StatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val imageCountryFlag: AppCompatImageView = itemView.findViewById(R.id.imageCountryFlag)
    private val textCountryName: TextView = itemView.findViewById(R.id.textCountryName)
    private val numberNewCases: TextView = itemView.findViewById(R.id.numberNewCases)
    private val numberNewDeaths: TextView = itemView.findViewById(R.id.numberNewDeaths)
    private val numberNewRecovered: TextView = itemView.findViewById(R.id.numberNewRecovered)
    private val numberTotalCases: TextView = itemView.findViewById(R.id.numberTotalCases)
    private val numberTotalDeaths: TextView = itemView.findViewById(R.id.numberTotalDeaths)
    private val numberTotalRecovered: TextView = itemView.findViewById(R.id.numberTotalRecovered)

    fun bindTo(stats: Stats) {
         val countryFlagResId = when (stats) {
            is GlobalStats ->  World.getWorldFlag()
            is CountryStats -> World.getFlagOf(stats.countryCode)
        }

        this.imageCountryFlag.setImageResource(countryFlagResId)
        textCountryName.text = stats.country
        numberNewCases.text = stats.newConfirmed.toString()
        numberNewDeaths.text = stats.newDeaths.toString()
        numberNewRecovered.text = stats.newRecovered.toString()
        numberTotalCases.text = stats.totalConfirmed.toString()
        numberTotalDeaths.text = stats.totalDeaths.toString()
        numberTotalRecovered.text = stats.totalRecovered.toString()
    }

}

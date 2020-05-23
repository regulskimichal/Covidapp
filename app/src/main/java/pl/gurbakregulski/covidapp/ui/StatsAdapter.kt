package pl.gurbakregulski.covidapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import pl.gurbakregulski.covidapp.R
import pl.gurbakregulski.covidapp.model.CountryStats
import pl.gurbakregulski.covidapp.model.GlobalStats
import pl.gurbakregulski.covidapp.model.Stats

class StatsAdapter : ListAdapter<Stats, StatsViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_stats, parent, false)

        return StatsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StatsViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Stats>() {
            override fun areItemsTheSame(oldItem: Stats, newItem: Stats): Boolean {
                return when {
                    oldItem is GlobalStats && newItem is GlobalStats -> true
                    oldItem is CountryStats && newItem is CountryStats && oldItem.countryCode == newItem.countryCode -> true
                    else -> false
                }
            }

            override fun areContentsTheSame(oldItem: Stats, newItem: Stats) = oldItem == newItem
        }
    }

}

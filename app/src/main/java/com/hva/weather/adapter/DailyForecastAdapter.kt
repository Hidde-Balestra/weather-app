package com.hva.weather.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hva.weather.R
import com.hva.weather.databinding.DailyForecastViewBinding
import com.hva.weather.model.DailyForecast

class DailyForecastAdapter(private val forecasts: List<DailyForecast>, private val clickListener: (DailyForecast) -> Unit) : RecyclerView.Adapter<DailyForecastAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = DailyForecastViewBinding.bind(itemView)

        fun databind(location: DailyForecast, clickListener: (DailyForecast) -> Unit) {
            location.drawableResourceId?.let { binding.ivForecastIcon.setImageResource(it) }
            if ((location.tempMeasurement ?: "C") == "C") {
                binding.tvMinAndMaxTemp.apply {
                    binding.tvMinAndMaxTemp.text = context.resources.getString(
                        R.string.tvMinAndMaxTemp,
                        location.minTempC,
                        location.tempMeasurement,
                        location.maxTempC,
                        location.tempMeasurement
                    )
                }
            } else {
                binding.tvMinAndMaxTemp.apply {
                    binding.tvMinAndMaxTemp.text = context.resources.getString(
                        R.string.tvMinAndMaxTemp,
                        location.minTempF,
                        location.tempMeasurement,
                        location.maxTempF,
                        location.tempMeasurement
                    )
                }
            }
            binding.tvForecastDate.apply {
                binding.tvForecastDate.text = context.resources.getString(
                    R.string.tvForecastDate,
                    location.date
                )
            }
            binding.tvRainChance.apply {
                binding.tvRainChance.text = context.resources.getString(
                    R.string.tvRainChance,
                    location.chanceOfRain
                )
            }
            itemView.setOnClickListener { clickListener(location)}
        }
    }

    /**
     * Creates and returns a ViewHolder object, inflating a standard layout called simple_list_item_1.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.daily_forecast_view, parent, false)
        )
    }

    /**
     * Returns the size of the list
     */
    override fun getItemCount(): Int {
        return forecasts.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(forecasts[position], clickListener)
    }
}

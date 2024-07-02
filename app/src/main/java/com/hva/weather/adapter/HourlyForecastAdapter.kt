package com.hva.weather.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hva.weather.R
import com.hva.weather.databinding.HourlyForecastViewBinding
import com.hva.weather.model.HourlyForecast

class HourlyForecastAdapter(private val forecasts: List<HourlyForecast>) : RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = HourlyForecastViewBinding.bind(itemView)

        @SuppressLint("StringFormatInvalid")
        fun databind(location: HourlyForecast) {
            location.drawableResourceId?.let { binding.ivForecastIcon.setImageResource(it) }
            if ((location.tempMeasurement ?: "C") == "C") {
                binding.tvForecastTemp.apply {
                    binding.tvForecastTemp.text = context.resources.getString(
                        R.string.tvForecastTemp,
                        location.TempC,
                        location.tempMeasurement
                    )
                }
            } else {
                binding.tvForecastTemp.apply {
                    binding.tvForecastTemp.text = context.resources.getString(
                        R.string.tvForecastTemp,
                        location.TempF,
                        location.tempMeasurement,
                    )
                }
            }
            binding.tvForecastTime.apply {
                binding.tvForecastTime.text = context.resources.getString(
                    R.string.tvForecastDate,
                    location.time
                )
            }
            binding.tvRainChance.apply {
                binding.tvRainChance.text = context.resources.getString(
                    R.string.tvRainChance,
                    location.chanceOfRain,
                )
            }
        }
    }

    /**
     * Creates and returns a ViewHolder object, inflating a standard layout called simple_list_item_1.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.hourly_forecast_view, parent, false)
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
        holder.databind(forecasts[position])
    }
}

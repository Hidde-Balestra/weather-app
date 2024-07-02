package com.hva.weather.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hva.weather.R
import com.hva.weather.databinding.LocationViewBinding
import com.hva.weather.model.Locations

class LocationListAdapter(private val locations: ArrayList<Locations>, private val clickListener: (Locations) -> Unit) : RecyclerView.Adapter<LocationListAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = LocationViewBinding.bind(itemView)

        fun databind(location: Locations, clickListener: (Locations) -> Unit) {
            binding.tvCity.text = location.city
            itemView.setOnClickListener { clickListener(location)}
        }
    }

    /**
     * Creates and returns a ViewHolder object, inflating a standard layout called simple_list_item_1.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.location_view, parent, false)
        )
    }

    /**
     * Returns the size of the list
     */
    override fun getItemCount(): Int {
        return locations.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(locations[position], clickListener)
    }
}

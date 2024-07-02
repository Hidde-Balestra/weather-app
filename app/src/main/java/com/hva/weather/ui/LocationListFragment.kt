package com.hva.weather.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hva.weather.adapter.LocationListAdapter
import com.hva.weather.viewModel.LocationViewModel
import com.hva.weather.R
import com.hva.weather.viewModel.WeatherViewModel
import com.hva.weather.databinding.FragmentLocationListBinding
import com.hva.weather.model.Locations

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class LocationListFragment : Fragment() {

    private var _binding: FragmentLocationListBinding? = null

    private val locationViewModel: LocationViewModel by activityViewModels()
    private val weatherViewModel: WeatherViewModel by activityViewModels()

    private val locations = arrayListOf<Locations>()
    private val locationListAdapter = LocationListAdapter(locations){locations: Locations -> locationItemClicked(locations)}

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private fun locationItemClicked(locations: Locations) {
        weatherViewModel.weatherFromCityLocation(locations.city)
        findNavController().navigate(R.id.WeatherViewFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLocationResult()
        initViews()

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.AddLocationListFragment)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeLocationResult() {
        locationViewModel.locations.observe(viewLifecycleOwner) { locations ->
            this@LocationListFragment.locations.clear()
            if (locations == null) {
                return@observe
            }
            if (weatherViewModel.firstLocation.value != null){
                weatherViewModel.firstLocation.value?.let {
                    this@LocationListFragment.locations.add(
                        it
                    )
                }
                binding.currentLocationImg.visibility = View.VISIBLE
            }
            this@LocationListFragment.locations.addAll(locations)
            locationListAdapter.notifyDataSetChanged()
        }
    }

    private fun initViews() {
        // Initialize the recycler view with a linear layout manager, adapter
        binding.rvLocationList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvLocationList.adapter = locationListAdapter
        binding.rvLocationList.addItemDecoration(DividerItemDecoration(this@LocationListFragment.context, DividerItemDecoration.VERTICAL))
        createItemTouchHelper().attachToRecyclerView(binding.rvLocationList)
    }

    private fun createItemTouchHelper(): ItemTouchHelper {
        // Callback which is used to create the ItemTouch helper. Only enables left swipe.
        // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            // Enables or Disables the ability to move items up and down.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            // Callback triggered when a user swiped an item.
            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val locationToDelete = locations[position]
                if (locationToDelete.isCurrentLocation){
                    locationListAdapter.notifyDataSetChanged()
                    return
                }
                locationViewModel.deleteLocation(locationToDelete)
                locationListAdapter.notifyItemRemoved(position)
            }
        }
        return ItemTouchHelper(callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
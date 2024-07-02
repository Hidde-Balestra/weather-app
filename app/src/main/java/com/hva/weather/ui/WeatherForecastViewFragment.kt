package com.hva.weather.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.*
import com.hva.weather.MainActivity
import com.hva.weather.R
import com.hva.weather.adapter.HourlyForecastAdapter
import com.hva.weather.databinding.FragmentForecastHourlyViewBinding
import com.hva.weather.model.HourlyForecast
import com.hva.weather.viewModel.SettingsViewModel
import com.hva.weather.viewModel.WeatherViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class WeatherForecastViewFragment : Fragment() {

    private var _binding: FragmentForecastHourlyViewBinding? = null
    private val viewModel: WeatherViewModel by activityViewModels()
    private val settingsViewModel: SettingsViewModel by activityViewModels()

    private var forecasts = ArrayList<HourlyForecast>()
    private val hourlyForecastAdapter = HourlyForecastAdapter(forecasts)

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForecastHourlyViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("FragmentLiveDataObserve", "NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.first_fragment_label,
            viewModel.currentLocation.value?.city ?: ""
        )
        initViews()
        observeForecastWeatherResult()
        settingsChange()

    }

    private fun initViews() {
        // Initialize the recycler view with a linear layout manager, adapter
        binding.rvHourlyForecast.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvHourlyForecast.adapter = hourlyForecastAdapter
        binding.rvHourlyForecast.addItemDecoration(DividerItemDecoration(this@WeatherForecastViewFragment.context, DividerItemDecoration.VERTICAL))
    }

    @SuppressLint("NotifyDataSetChanged", "DiscouragedApi")
    private fun observeForecastWeatherResult() {
        forecasts.clear()

        viewModel.hourlyForecast.forEach{ i ->
            val drawableResourceId =
                this.resources.getIdentifier("_${i.icon}", "drawable", activity?.packageName)
            i.drawableResourceId = drawableResourceId
        }

        forecasts.addAll(viewModel.hourlyForecast.filter { it.dayId == viewModel.dayId } as ArrayList<HourlyForecast>)
        hourlyForecastAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun settingsChange() {
        settingsViewModel.settings.observe(viewLifecycleOwner) { settings ->
            if (settings == null) {
                return@observe
            }
            forecasts.forEach { it.tempMeasurement = settings.tempMeasurement }
            hourlyForecastAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

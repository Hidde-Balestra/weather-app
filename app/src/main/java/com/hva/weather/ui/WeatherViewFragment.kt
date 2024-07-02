package com.hva.weather.ui

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.hva.weather.MainActivity
import com.hva.weather.R
import com.hva.weather.adapter.DailyForecastAdapter
import com.hva.weather.databinding.FragmentWeatherViewBinding
import com.hva.weather.model.DailyForecast
import com.hva.weather.model.Settings
import com.hva.weather.viewModel.SettingsViewModel
import com.hva.weather.viewModel.WeatherViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class WeatherViewFragment : Fragment() {

    private var _binding: FragmentWeatherViewBinding? = null
    private val viewModel: WeatherViewModel by activityViewModels()
    private val settingsViewModel: SettingsViewModel by activityViewModels()

    private val forecasts = arrayListOf<DailyForecast>()
    private val forecastAdapter = DailyForecastAdapter(forecasts){dailyForecast: DailyForecast -> forecastItemClicked(dailyForecast)}

    private fun forecastItemClicked(dailyForecast: DailyForecast) {
        viewModel.dayId = dailyForecast.id
        println(viewModel.dayId)
        if (viewModel.dayId == -1){
            return
        }
        findNavController().navigate(R.id.WeatherForecastViewFragment)
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherViewBinding.inflate(inflater, container, false)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.first_fragment_label, "")
        return binding.root
    }

    @SuppressLint("FragmentLiveDataObserve", "NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (settingsViewModel.settings.value == null){
            settingsViewModel.updateSettings(Settings("C", "km",0))
        }
        binding.weatherLoading.visibility = View.VISIBLE
        viewModel.dailyForecast.value = null
        initViews()
        observeCurrentWeatherResult()
        observeForecastWeatherResult()
        settingsChange()

        binding.weatherViewFab.setOnClickListener {
            findNavController().navigate(R.id.LocationListFragment)
        }
    }

    private fun showDialog(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.unknownPlaceErrorTitle))
        val dialogLayout = layoutInflater.inflate(R.layout.error_unknown_place, null)

        builder.setView(dialogLayout)
        builder.setPositiveButton(R.string.btnOK) { _: DialogInterface, _: Int ->
        }
        builder.show()
    }

    private fun initViews() {
        // Initialize the recycler view with a linear layout manager, adapter
        binding.rvDailyForecast.layoutManager = StaggeredGridLayoutManager(3, GridLayoutManager.VERTICAL)
        binding.rvDailyForecast.adapter = forecastAdapter
    }

    @SuppressLint("DiscouragedApi", "StringFormatInvalid")
    private fun observeCurrentWeatherResult() {
        viewModel.currentWeather.observe(viewLifecycleOwner, Observer { currentWeather ->
            binding.weatherLoading.visibility = View.INVISIBLE
            if (currentWeather == null) {
                if(viewModel.errorCode == 400){
                    showDialog()
                    viewModel.errorCode = -1
                    findNavController().navigate(R.id.LocationListFragment)
                }
                if(viewModel.errorCode == 0){
                    viewModel.errorCode = -1
                    findNavController().navigate(R.id.LocationListFragment)
                }
                return@Observer
            }
//            get img id from img name
            val drawableResourceId = resources.getIdentifier("_${currentWeather.icon}", "drawable",
                activity?.packageName
            )
            binding.currentWeatherGroup.visibility = View.VISIBLE
            (activity as MainActivity).supportActionBar?.title = getString(R.string.first_fragment_label, viewModel.currentLocation.value?.city)
            if ((settingsViewModel.settings.value?.tempMeasurement ?: "C") == "C"){
                binding.tvTemp.text = getString(R.string.tvCurrentTemp, currentWeather.tempC, settingsViewModel.settings.value?.tempMeasurement)
                binding.tvFeelsLike.text = getString(R.string.feelslike, currentWeather.feelsLikeC, settingsViewModel.settings.value?.tempMeasurement)
            } else {
                binding.tvTemp.text = getString(R.string.tvCurrentTemp, currentWeather.tempF, settingsViewModel.settings.value?.tempMeasurement)
                binding.tvFeelsLike.text = getString(R.string.feelslike, currentWeather.feelsLikeF, settingsViewModel.settings.value?.tempMeasurement)
            }

            if ((settingsViewModel.settings.value?.distanceMeasurement ?: "km") == "km"){
                binding.tvWindSpeed.text = getString(R.string.tvCurrentWindSpeed, currentWeather.windKph, "${settingsViewModel.settings.value?.distanceMeasurement}/h")
                binding.tvVis.text = getString(R.string.vis, currentWeather.visKm, settingsViewModel.settings.value?.distanceMeasurement)
            } else {
                binding.tvWindSpeed.text = getString(R.string.tvCurrentWindSpeed, currentWeather.windMph, "${settingsViewModel.settings.value?.distanceMeasurement}/h")
                binding.tvVis.text = getString(R.string.vis, currentWeather.visMiles, settingsViewModel.settings.value?.distanceMeasurement)
            }

            binding.ivCurrentWeather.setImageResource(drawableResourceId)
            binding.tvUv.text = getString(R.string.uv, currentWeather.uv)
            binding.tvHumidity.text = getString(R.string.humidity, currentWeather.humidity)
            binding.tvPressure.text = getString(R.string.pressure_mb, currentWeather.pressure)

            binding.tvWindDir.text = getString(R.string.wind_dir, currentWeather.windDir)

            binding.tvLastUpdated.text = getString(R.string.tvLastUpdated, currentWeather.updateTime)
        })
    }

    @SuppressLint("NotifyDataSetChanged", "DiscouragedApi")
    private fun observeForecastWeatherResult() {
        viewModel.dailyForecast.observe(viewLifecycleOwner) { forecast ->
            if (forecast == null) {
                return@observe
            }
            val drawableResourceId =
                this.resources.getIdentifier("_${forecast.icon}", "drawable", activity?.packageName)
            forecast.drawableResourceId = drawableResourceId
            forecast.tempMeasurement = settingsViewModel.settings.value?.tempMeasurement
            viewModel.dailyForecast.value?.let {
                this@WeatherViewFragment.forecasts.add(
                    forecast
                )
            }
            forecastAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun settingsChange() {
        settingsViewModel.settings.observe(viewLifecycleOwner) { settings ->
            if (settings == null) {
                return@observe
            }
            forecasts.forEach { it.tempMeasurement = settings.tempMeasurement }
            forecastAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

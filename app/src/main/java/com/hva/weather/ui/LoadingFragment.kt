package com.hva.weather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.hva.weather.R
import com.hva.weather.databinding.FragmentLoadingBinding
import com.hva.weather.viewModel.SettingsViewModel
import com.hva.weather.viewModel.WeatherViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class LoadingFragment : Fragment() {
    private var _binding: FragmentLoadingBinding? = null

    private val viewModelWeather: WeatherViewModel by activityViewModels()
    private val viewModelSettings: SettingsViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelWeather.geoLocation.observe(viewLifecycleOwner, Observer { geoLocation ->
            if (geoLocation == null) {
                return@Observer
            }
            if (!geoLocation.isEnabled){
                findNavController().navigate(R.id.LocationListFragment)
                return@Observer
            }
            findNavController().navigate(R.id.WeatherViewFragment)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
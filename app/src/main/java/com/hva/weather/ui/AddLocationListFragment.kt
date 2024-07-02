package com.hva.weather.ui

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.hva.weather.viewModel.LocationViewModel
import com.hva.weather.R
import com.hva.weather.databinding.FragmentAddLocationListBinding
import com.hva.weather.model.Locations
import com.hva.weather.viewModel.WeatherViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddLocationListFragment : Fragment() {

    private var _binding: FragmentAddLocationListBinding? = null
    private val viewModel: LocationViewModel by viewModels()
    private val weatherviewModel: WeatherViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddLocationListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherviewModel.currentLocation.value = null
        observeCurrentLocationResult()

        binding.btnSave.setOnClickListener {
            weatherviewModel.checkIfCityExist(binding.cityLocationField.text.toString())
        }
    }

    private fun unknownCity(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.unknownPlaceErrorTitle))
        val dialogLayout = layoutInflater.inflate(R.layout.error_unknown_place, null)

        builder.setView(dialogLayout)
        builder.setPositiveButton(R.string.btnOK) { _: DialogInterface, _: Int ->
        }
        builder.show()
    }

    @SuppressLint("DiscouragedApi", "StringFormatInvalid")
    private fun observeCurrentLocationResult() {
        weatherviewModel.currentLocation.observe(viewLifecycleOwner, Observer { currentLocation ->
            if (currentLocation == null) {
                if(weatherviewModel.errorCode == 400){
                    unknownCity()
                    weatherviewModel.errorCode = -1
                }
                return@Observer
            }
            viewModel.createLocation(Locations(binding.cityLocationField.text.toString(), false))
            findNavController().navigate(R.id.LocationListFragment)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
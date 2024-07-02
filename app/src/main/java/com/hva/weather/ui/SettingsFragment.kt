package com.hva.weather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.hva.weather.databinding.FragmentSettingsBinding
import com.hva.weather.model.Settings
import com.hva.weather.viewModel.SettingsViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val viewModel: SettingsViewModel by activityViewModels()

    private lateinit var tempChecked: RadioButton
    private lateinit var distanceChecked: RadioButton

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSettingsResult()

        binding.settingsSaveBtn.setOnClickListener {
            val tempCheckedId = binding.radioGroup1.checkedRadioButtonId
            val distanceCheckedId = binding.radioGroup2.checkedRadioButtonId
            tempChecked = binding.radioGroup1.findViewById(tempCheckedId)
            distanceChecked = binding.radioGroup2.findViewById(distanceCheckedId)
            updateSettings()
        }
    }

    private fun observeSettingsResult() {
        viewModel.settings.observe(viewLifecycleOwner) { settings ->
            if (settings.tempMeasurement == "C"){
                binding.cButton.isChecked = true
            } else {
                binding.fButton.isChecked = true
            }

            if (settings.distanceMeasurement == "km"){
                binding.kmButton.isChecked = true
            } else {
                binding.milesButton.isChecked = true
            }
        }
    }

    private fun updateSettings() {
        viewModel.updateSettings(Settings(tempChecked.text as String, distanceChecked.text as String, 0))
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
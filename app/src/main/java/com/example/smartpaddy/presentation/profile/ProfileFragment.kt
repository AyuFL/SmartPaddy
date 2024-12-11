package com.example.smartpaddy.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.smartpaddy.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

  private lateinit var binding: FragmentProfileBinding
  private val viewModel: ProfileViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentProfileBinding.inflate(layoutInflater)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val switchTheme = binding.themeSwitch

    viewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
      if (isDarkModeActive) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        switchTheme.isChecked = true
      } else {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        switchTheme.isChecked = false
      }
    }

    switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
      viewModel.saveThemeSetting(isChecked)
    }
  }
}
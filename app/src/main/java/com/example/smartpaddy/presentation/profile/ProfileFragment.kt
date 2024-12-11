package com.example.smartpaddy.presentation.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.smartpaddy.databinding.FragmentProfileBinding
import com.example.smartpaddy.presentation.login.LoginActivity
import com.example.smartpaddy.utils.Constants

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

    showUserInfo()

    binding.apply {
      logoutBtn.setOnClickListener {
        logoutUser(requireContext())
      }
    }

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

  private fun showUserInfo() {
    val sharedPreferences =
      requireContext().getSharedPreferences(Constants.login, Context.MODE_PRIVATE)
    val token = sharedPreferences.getString(Constants.token, null)
    val name = sharedPreferences.getString(Constants.name, "Guest")
    val email = sharedPreferences.getString(Constants.email, "Email")

    if (token != null) {
      binding.apply {
        userNameTv.text = name
        userEmailTv.text = email
      }
    } else {
      binding.apply {
        userNameTv.text = null
        userEmailTv.text = null
      }
    }
  }

  private fun logoutUser(context: Context) {
    val sharedPreferences = context.getSharedPreferences(Constants.login, Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
      remove(Constants.name)
      remove(Constants.email)
      remove(Constants.token)
      remove(Constants.login)
      apply()
    }
    val intent = Intent(context, LoginActivity::class.java)
    startActivity(intent)
  }
}
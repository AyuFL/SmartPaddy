package com.example.smartpaddy.presentation.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.smartpaddy.R
import com.example.smartpaddy.databinding.ActivityMainBinding
import com.example.smartpaddy.presentation.camera.CameraFragment
import com.example.smartpaddy.presentation.home.HomeFragment
import com.example.smartpaddy.presentation.profile.ProfileFragment
import com.example.smartpaddy.presentation.profile.ProfileViewModel

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private val profileViewModel: ProfileViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    binding = ActivityMainBinding.inflate(layoutInflater)

    setContentView(binding.root)
    ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets
    }

    setupView()
  }

  private fun setupView() {
    val fragments = listOf(
      HomeFragment(),
      CameraFragment(),
      ProfileFragment()
    )

    profileViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
      if (isDarkModeActive) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
      } else {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
      }
    }

    val adapter = ViewPagerAdapter(this, fragments)
    val viewPager = binding.viewPager

    viewPager.adapter = adapter

    binding.bottomNav.setOnItemSelectedListener { menuItem ->
      when (menuItem.itemId) {
        R.id.home -> viewPager.currentItem = 0
        R.id.camera -> viewPager.currentItem = 1
        R.id.profile -> viewPager.currentItem = 2
      }
      true
    }

    viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
      override fun onPageSelected(position: Int) {
        binding.bottomNav.menu.getItem(position).isChecked = true
      }
    })
  }
}
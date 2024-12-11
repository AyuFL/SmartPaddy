package com.example.smartpaddy.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.smartpaddy.R
import com.example.smartpaddy.databinding.ActivityMainBinding
import com.example.smartpaddy.presentation.camera.CameraActivity
import com.example.smartpaddy.presentation.home.HomeFragment
import com.example.smartpaddy.presentation.profile.ProfileFragment

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    binding = ActivityMainBinding.inflate(layoutInflater)

    setContentView(binding.root)

    ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
      insets
    }

    setupView()
  }

  private fun setupView() {
    val fragments = listOf(
      HomeFragment(),
      ProfileFragment()
    )

    val adapter = ViewPagerAdapter(this, fragments)
    val viewPager = binding.viewPager

    viewPager.adapter = adapter

    binding.bottomNav.setOnItemSelectedListener { menuItem ->
      when (menuItem.itemId) {
        R.id.home -> viewPager.currentItem = 0
        R.id.profile -> viewPager.currentItem = 1
      }
      true
    }

    binding.fab.setOnClickListener {
      val intent = Intent(this@MainActivity, CameraActivity::class.java)
      startActivity(intent)
    }


    viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
      override fun onPageSelected(position: Int) {
        binding.bottomNav.menu.getItem(position).isChecked = true
      }
    })
  }
}
package com.example.smartpaddy.presentation.mainPage

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.smartpaddy.R
import com.example.smartpaddy.data.network.ConnectivityObserver
import com.example.smartpaddy.data.network.NetworkUtils
import com.example.smartpaddy.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  private val mainActivityViewModel: MainActivityViewModel by viewModels()

  private var navController: NavController? = null

  private lateinit var connectivityObserver: ConnectivityObserver

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    checkConnectivityStatus()
  }

  private fun checkConnectivityStatus() {
    lifecycleScope.launch {
      connectivityObserver.observe().collectLatest {
        if (it.toString() == getString(R.string.available) &&
          NetworkUtils.isConnectedToNetwork.value != true
        ) {
          NetworkUtils.setConnectionToTrue()
        } else if (it.toString() != getString(R.string.available) &&
          NetworkUtils.isConnectedToNetwork.value != false
        ) {
          NetworkUtils.setConnectionToFalse()
        }
      }
    }
  }
}


package com.example.smartpaddy.presentation.history

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartpaddy.databinding.ActivityHistoryBinding
import com.example.smartpaddy.presentation.history.adapter.HistoryAdapter
import com.example.smartpaddy.utils.Constants

class HistoryActivity : AppCompatActivity() {

  private lateinit var binding: ActivityHistoryBinding
  private val viewModel: HistoryViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    binding = ActivityHistoryBinding.inflate(layoutInflater)

    setContentView(binding.root)
    ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets
    }

    setupView()
  }

  private fun setupView() {
    val token = getToken()

    if (token != "token" && token != null) {
      viewModel.getHistory(token)
    }

    val adapter = HistoryAdapter()

    viewModel.history.observe(this) { history ->
      showLoading(false)
      adapter.setHistoryList(history.data)
    }

    viewModel.isLoading.observe(this) { isLoading ->
      showLoading(isLoading)
    }

    binding.historyRv.adapter = adapter
    binding.historyRv.layoutManager = LinearLayoutManager(this)
  }

  private fun showLoading(isLoading: Boolean) {
//    binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
  }

  private fun getToken(): String? {
    val sharedPreferences = getSharedPreferences(Constants.login, MODE_PRIVATE)
    return sharedPreferences.getString(Constants.token, null)
  }
}
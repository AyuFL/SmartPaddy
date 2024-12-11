package com.example.smartpaddy.presentation.historyDetail

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.smartpaddy.databinding.ActivityHistoryDetailBinding

class HistoryDetailActivity : AppCompatActivity() {

  private lateinit var binding: ActivityHistoryDetailBinding
  private val viewModel: HistoryDetailViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    binding = ActivityHistoryDetailBinding.inflate(layoutInflater)
    setContentView(binding.root)

    ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets
    }

    setupView()
    observeViewModel()
  }

  private fun setupView() {
    val postId = intent.getStringExtra("postId")
    val imageUrl = intent.getStringExtra("imageUrl")

    if (postId != null) {
      viewModel.getHistoryDetail(postId)
    }

    viewModel.history.observe(this) { history ->
      binding.accuracyTv.text = "${(history.predictedProb * 100).toString().take(2)}%"
      binding.classTv.text = history.predictedClass

      binding.penjelasanTv.text = formatParagraph(history.penjelasan)
      binding.gejalaTv.text = formatParagraph(history.gejala)
      binding.caraTv.text = formatParagraph(history.caraMenangani)

      Glide.with(binding.root)
        .load(imageUrl)
        .into(binding.paddyIv)
    }
  }

  private fun observeViewModel() {
    viewModel.isLoading.observe(this) { isLoading ->
      showLoading(isLoading)
    }
  }

  private fun showLoading(isLoading: Boolean) {
    binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    binding.paddyIv.visibility = if (isLoading) View.GONE else View.VISIBLE
    binding.caraTitle.visibility = if (isLoading) View.GONE else View.VISIBLE
    binding.gejalaTitle.visibility = if (isLoading) View.GONE else View.VISIBLE
    binding.penjelasanTitle.visibility = if (isLoading) View.GONE else View.VISIBLE
  }

  private fun formatParagraph(input: String): String {
    val regex = "(?<=\\s)(\\d+\\.|[a-zA-Z]\\.)".toRegex()
    return input.replace(regex) { matchResult ->
      if (matchResult.value.matches(Regex("\\d+\\."))) {
        "\n${matchResult.value}"
      } else {
        "\n\t${matchResult.value}"
      }
    }
  }
}
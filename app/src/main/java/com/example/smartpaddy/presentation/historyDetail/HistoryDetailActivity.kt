package com.example.smartpaddy.presentation.historyDetail

import android.os.Bundle
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

  private fun formatParagraph(input: String): String {
    val regex = "(?<=\\s)(\\d+\\.|[a-zA-Z]\\.)".toRegex()

    return input.replace(regex) { matchResult ->
      if (matchResult.value.matches(Regex("\\d+\\."))) {
        return@replace "\n${matchResult.value}"
      } else {
        return@replace "\n\t${matchResult.value}"
      }
    }
  }
}
package com.example.smartpaddy.presentation.historyDetail

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.smartpaddy.R
import com.example.smartpaddy.databinding.ActivityHistoryDetailBinding
import com.example.smartpaddy.presentation.history.adapter.HistoryAdapter

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

    binding.btnBack.setOnClickListener {
      finish()
    }

    setupView()
  }

  private fun setupView() {
    val postId = intent.getStringExtra("postId")
    val imageUrl = intent.getStringExtra("imageUrl")

    if (postId != null) {
      Log.e("bella", "fetch detail")
      viewModel.getHistoryDetail(postId)
    }

    viewModel.history.observe(this) { history ->
      Log.e("bella", "ini class ${history.predictedClass}")
      binding.accuracyTv.text = "${(history.predictedProb * 100).toString().take(2)}%"
      binding.classTv.text = history.predictedClass
      binding.descTv.text = formatToNumberedList(history.caraMenangani)

      Glide.with(binding.root)
        .load(imageUrl)
        .into(binding.paddyIv)
    }

    viewModel.isLoading.observe(this) { isLoading ->
      showLoading(isLoading)
    }

    viewModel.message.observe(this) { message ->
      Log.e("bella", "ini message $message")
    }
  }

  private fun showLoading(isLoading: Boolean) {
//    binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
  }

  fun formatToNumberedList(paragraph: String): String {
    // Split the paragraph into individual points based on the pattern of numbered points
    val points = paragraph.split(Regex("""\d+\.\s"""))
      .filter { it.isNotBlank() } // Remove any empty results from split

    // Format each point into a numbered list
    val formattedList = points.mapIndexed { index, point ->
      "${index + 1}. ${point.trim()}"
    }

    // Join the list back into a single string with line breaks
    return formattedList.joinToString("\n")
  }
}
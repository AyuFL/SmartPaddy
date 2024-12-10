package com.example.smartpaddy.presentation.homePage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpaddy.databinding.FragmentHomeBinding
import com.example.smartpaddy.ui.history.HistoryActivity
import com.example.smartpaddy.ui.home.adapter.HomeAdapter

class HomeFragment : Fragment() {

  private lateinit var binding: FragmentHomeBinding
  private val viewModel: HomeViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentHomeBinding.inflate(layoutInflater)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel.getHistory()

    val adapter = HomeAdapter()

    viewModel.history.observe(viewLifecycleOwner) { history ->
      showLoading(false)
      Log.e("bella", "${history.data}")
      binding.paddyCountTv.text = history.data.size.toString()

      adapter.setHistoryList(history.data)
    }

    viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
      showLoading(isLoading)
    }

    binding.historyRv.adapter = adapter
    binding.historyRv.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

    binding.historyBtnTv.setOnClickListener {
      val intent = Intent(context, HistoryActivity::class.java)
      startActivity(intent)
    }
  }

  private fun showLoading(isLoading: Boolean) {
//    binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
  }
}
package com.example.smartpaddy.presentation.home

import android.content.Context.MODE_PRIVATE
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpaddy.R
import com.example.smartpaddy.databinding.FragmentHomeBinding
import com.example.smartpaddy.presentation.history.HistoryActivity
import com.example.smartpaddy.presentation.home.adapter.HomeAdapter
import com.example.smartpaddy.presentation.login.LoginActivity
import com.example.smartpaddy.utils.Constants

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

    fetchData()
    greetings()

    val adapter = HomeAdapter()

    viewModel.history.observe(viewLifecycleOwner) { history ->
      showLoading(false)
      binding.paddyCountTv.text = history.data.size.toString()

      adapter.setHistoryList(history.data)
    }

    viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
      showLoading(isLoading)
    }

    binding.historyRv.adapter = adapter
    binding.historyRv.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

    binding.apply {
      historyBtnTv.setOnClickListener {
        val intent = Intent(context, HistoryActivity::class.java)
        startActivity(intent)
      }
    }

    binding.btnLogout.setOnClickListener {
      logoutUser(requireContext())
    }
  }

  private fun greetings() {
    val sharedPreferences = requireContext().getSharedPreferences(Constants.login, Context.MODE_PRIVATE)
    val token = sharedPreferences.getString(Constants.token, null)
    val name = sharedPreferences.getString(Constants.name, "Guest")

    if (token != null) {
      binding.tvUsername.text = getString(R.string.greetings, name)
    } else {
      binding.tvUsername.text = getString(R.string.greetings, "Guest")
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

  private fun showLoading(isLoading: Boolean) {
//    binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
  }

  private fun getToken(): String? {
    val sharedPreferences = requireContext().getSharedPreferences(Constants.login, MODE_PRIVATE)
    return sharedPreferences.getString(Constants.token, null)
  }

  override fun onResume() {
    super.onResume()
    fetchData()
  }

  private fun fetchData() {
    val token = getToken()

    if (token != "token" && token != null) {
      viewModel.getHistory(token)
    }
  }
}
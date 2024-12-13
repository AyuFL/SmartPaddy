package com.example.smartpaddy.presentation.home

import android.content.Context
import android.content.Context.MODE_PRIVATE
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
import com.example.smartpaddy.presentation.camera.CameraActivity
import com.example.smartpaddy.presentation.history.HistoryActivity
import com.example.smartpaddy.presentation.home.adapter.HomeAdapter
import com.example.smartpaddy.presentation.login.LoginActivity
import com.example.smartpaddy.utils.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.Locale

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

    if (viewModel.history.value?.data.isNullOrEmpty()) {
      historyIsEmpty()
    }

    viewModel.history.observe(viewLifecycleOwner) { history ->
      showLoading(false)

      if (history.data.isNotEmpty()) {
        binding.paddyCountTv.text = history.data.size.toString()
        historyIsNotEmpty()

        adapter.setHistoryList(history.data)
      }
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
    val sharedPreferences = requireContext().getSharedPreferences(Constants.login, MODE_PRIVATE)
    val token = sharedPreferences.getString(Constants.token, null)
    val name = sharedPreferences.getString(Constants.name, "Guest")
      ?.split(" ")
      ?.joinToString(" ") { it -> it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }

    if (token != null) {
      binding.tvUsername.text = getString(R.string.greetings, name)
    } else {
      binding.tvUsername.text = getString(R.string.greetings, "Guest")
    }
  }

  private fun logoutUser(context: Context) {
    MaterialAlertDialogBuilder(context)
      .setTitle(getString(R.string.logout_confirmation))
      .setMessage(getString(R.string.logout_message))
      .setPositiveButton(getString(R.string.yes)) { _, _ ->
        val sharedPreferences = context.getSharedPreferences(Constants.login, MODE_PRIVATE)
        with(sharedPreferences.edit()) {
          remove(Constants.name)
          remove(Constants.email)
          remove(Constants.token)
          remove(Constants.login)
          apply()
        }
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
      }
      .setNegativeButton(getString(R.string.no)) { dialog, _ ->
        dialog.dismiss()
      }
      .show()
  }

  private fun showLoading(isLoading: Boolean) {
    binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
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

  private fun historyIsEmpty() {
    binding.homeTitleTv.text = getString(R.string.wow_i_guess_you_have_a_healthy_rice_fields)
    binding.cameraBtn.visibility = View.VISIBLE
    binding.historyBtnTv.visibility = View.GONE
    binding.historyRv.visibility = View.GONE

    binding.cameraBtn.setOnClickListener {
      val intent = Intent(requireContext(), CameraActivity::class.java)
      startActivity(intent)
    }
  }

  private fun historyIsNotEmpty() {
    binding.homeTitleTv.text = getString(R.string.you_have_been_detected)
    binding.homeSubtitleTv.visibility = View.VISIBLE
    binding.cameraBtn.visibility = View.GONE
    binding.historyBtnTv.visibility = View.VISIBLE
    binding.historyRv.visibility = View.VISIBLE
  }
}
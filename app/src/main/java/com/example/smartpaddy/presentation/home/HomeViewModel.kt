package com.example.smartpaddy.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpaddy.data.response.HistoryResponse
import com.example.smartpaddy.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel : ViewModel() {
  private val _history = MutableLiveData<HistoryResponse>()
  val history: LiveData<HistoryResponse> = _history

  private val _isLoading = MutableLiveData<Boolean>()
  val isLoading: LiveData<Boolean> = _isLoading

  fun getHistory(token: String) {
    _isLoading.value = true
    viewModelScope.launch {
      try {
        val apiService = ApiConfig.getApiService()
        val response = apiService.getHistory(token)

        if (response.status == "success") {
          _history.postValue(response)
        }
      } catch (e: HttpException) {
        Log.e("bella", e.toString())
      } finally {
        _isLoading.postValue(false)
      }
    }
  }
}
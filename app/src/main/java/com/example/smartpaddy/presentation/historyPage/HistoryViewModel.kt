package com.example.smartpaddy.ui.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpaddy.data.response.HistoryResponse
import com.example.smartpaddy.data.retrofit.ApiConfig
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HistoryViewModel : ViewModel() {
  private val _history = MutableLiveData<HistoryResponse>()
  val history: LiveData<HistoryResponse> = _history

  private val _isLoading = MutableLiveData<Boolean>()
  val isLoading: LiveData<Boolean> = _isLoading

  fun getHistory() {
    _isLoading.value = true
    viewModelScope.launch {
      try {
        val apiService = ApiConfig.getApiService()
        val response = apiService.getHistory("kxWXlxIxO9dX3TJE")

        if (response.status == "success") {
          _history.postValue(response)
        }
      } catch (e: HttpException) {
        val jsonInString = e.response()?.errorBody()?.string()
        val errorBody = Gson().fromJson(jsonInString, HistoryResponse::class.java)
        val errorMessage = errorBody.message

        if (errorMessage != null) {
          Log.e("bella", errorMessage)
        }
      } finally {
        _isLoading.postValue(false)
      }
    }
  }
}
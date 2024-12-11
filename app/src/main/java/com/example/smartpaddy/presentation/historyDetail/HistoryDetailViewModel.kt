package com.example.smartpaddy.presentation.historyDetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpaddy.data.response.ResultResponse
import com.example.smartpaddy.data.retrofit.ApiConfig
import com.example.smartpaddy.utils.parseErrorResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HistoryDetailViewModel : ViewModel() {
  private val _history = MutableLiveData<ResultResponse>()
  val history: LiveData<ResultResponse> = _history

  private val _isLoading = MutableLiveData<Boolean>()
  val isLoading: LiveData<Boolean> = _isLoading

  fun getHistoryDetail(postId: String) {
    _isLoading.value = true
    viewModelScope.launch {
      try {
        val apiService = ApiConfig.getApiService()
        val response = apiService.getDetailPost(postId)

        if (response.status == "success" && response.data != null) {
          _history.value = response.data.result
        }
      } catch (e: Exception) {
        var errorMessage = "An unexpected error occurred"
        if (e is HttpException) {
          val errorBody = e.response()?.errorBody()?.string()
          errorMessage = parseErrorResponse(errorBody)?.message ?: "Error: ${e.message()}"
        }
        Log.e("bella", errorMessage)
      } finally {
        _isLoading.value = false
      }
    }
  }
}

package com.example.smartpaddy.presentation.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpaddy.data.model.UserModel
import com.example.smartpaddy.data.response.HistoryResponse
import com.example.smartpaddy.data.retrofit.ApiConfig
import com.example.smartpaddy.utils.Constants
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel : ViewModel() {
  private val _history = MutableLiveData<HistoryResponse>()
  val history: LiveData<HistoryResponse> = _history

  private val _isLoading = MutableLiveData<Boolean>()
  val isLoading: LiveData<Boolean> = _isLoading

  private val _userSession = MutableLiveData<UserModel>()
  val userSession: LiveData<UserModel> = _userSession

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

  fun fetchUserDetails(context: Context) {
    val sharedPreferences = context.getSharedPreferences(Constants.login, Context.MODE_PRIVATE)
    val token = sharedPreferences.getString(Constants.token, null)
    val userName = sharedPreferences.getString(Constants.name, null)
    val userEmail = sharedPreferences.getString(Constants.email, null)

    if (token.isNullOrEmpty()) {
      _userSession.postValue(UserModel(name = "Guest", email = null, token = null))
      return
    }

    _userSession.postValue(
      UserModel(
        name = userName ?: "Guest",
        email = userEmail,
        token = token
      )
    )
  }
}
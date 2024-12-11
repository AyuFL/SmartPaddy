package com.example.smartpaddy.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpaddy.data.request.LoginRequest
import com.example.smartpaddy.data.response.LoginResponse
import com.example.smartpaddy.data.retrofit.ApiConfig
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

  private val _loginResponse = MutableLiveData<LoginResponse>()
  val loginResponse: LiveData<LoginResponse> = _loginResponse

  fun login(email: String, password: String) {
    viewModelScope.launch {
      try {
        val apiService = ApiConfig.getApiService()
        val loginRequest = LoginRequest(email, password)
        val response = apiService.login(loginRequest)

        if (response.status == "success") {
          _loginResponse.value = LoginResponse(
            status = "success",
            message = response.message,
            token = response.token,
            name = response.name
          )
        } else {
          _loginResponse.value = LoginResponse(status = "fail", message = response.message)
        }
      } catch (e: Exception) {
        _loginResponse.value = LoginResponse(
          status = "fail",
          message = "Login failed. Please try again."
        )
      }
    }
  }
}

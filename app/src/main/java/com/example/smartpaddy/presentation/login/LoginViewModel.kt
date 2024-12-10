package com.example.smartpaddy.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpaddy.data.response.LoginResponse
import com.example.smartpaddy.data.retrofit.ApiConfig
import com.example.smartpaddy.data.request.LoginRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel : ViewModel() {

  private val _loginResponse = MutableLiveData<LoginResponse>()
  val loginResponse: LiveData<LoginResponse> = _loginResponse

  fun login(email: String, password: String) {
    viewModelScope.launch {
      try {
        val apiService = ApiConfig.getApiService()
        val loginRequest = LoginRequest(email, password)
        val response = apiService.login(loginRequest)
        _loginResponse.value = response
      } catch (e: HttpException) {
        val errorBody = e.response()?.errorBody()?.string()
        _loginResponse.value = LoginResponse(
          message = "Login failed: Incorrect email or password",
          status = "fail",
          token = ""
        )
      } catch (e: IOException) {
        _loginResponse.value = LoginResponse(
          message = "Network error: Check internet connection",
          status = "fail",
          token = ""
        )
      } catch (e: Exception) {
        _loginResponse.value = LoginResponse(
          message = "Unexpected error: ${e.message}",
          status = "fail",
          token = ""
        )
      }
    }
  }
}

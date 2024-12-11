package com.example.smartpaddy.presentation.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpaddy.data.request.RegisterRequest
import com.example.smartpaddy.data.response.RegisterResponse
import com.example.smartpaddy.data.response.UserResponse
import com.example.smartpaddy.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class RegisterViewModel : ViewModel() {

  private val _registerResponse = MutableLiveData<RegisterResponse>()
  val registerResponse: LiveData<RegisterResponse> = _registerResponse

  fun register(name: String, email: String, password: String) {
    viewModelScope.launch {
      try {
        val apiService = ApiConfig.getApiService()
        val registerRequest = RegisterRequest(name, email, password)
        val response = apiService.register(registerRequest)
        _registerResponse.value = response
      } catch (e: HttpException) {
        _registerResponse.value = RegisterResponse(
          message = e.response()?.errorBody()?.string()
            ?: "Registration failed",
          status = "fail",
          user = UserResponse("", "", "")
        )
      } catch (e: IOException) {
        _registerResponse.value = RegisterResponse(
          message = "Network error: Check internet connection",
          status = "fail",
          user = UserResponse("", "", "")
        )
      } catch (e: Exception) {
        _registerResponse.value = RegisterResponse(
          message = "Unexpected error: ${e.message}",
          status = "fail",
          user = UserResponse("", "", "")
        )
        Log.d("regist", "${e.message}")
      }
    }
  }
}
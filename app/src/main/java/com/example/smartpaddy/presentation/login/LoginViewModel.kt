package com.example.smartpaddy.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpaddy.data.request.LoginRequest
import com.example.smartpaddy.data.response.LoginResponse
import com.example.smartpaddy.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
<<<<<<< HEAD
import retrofit2.HttpException
import java.io.IOException
=======
>>>>>>> refs/heads/md-bella

class LoginViewModel : ViewModel() {

  private val _loginResponse = MutableLiveData<LoginResponse>()
  val loginResponse: LiveData<LoginResponse> = _loginResponse

  fun login(email: String, password: String) {
    viewModelScope.launch {
      try {
        val apiService = ApiConfig.getApiService()
        val loginRequest = LoginRequest(email, password)
        val response = apiService.login(loginRequest)
<<<<<<< HEAD
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
=======

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
>>>>>>> refs/heads/md-bella
        )
      }
    }
  }
}

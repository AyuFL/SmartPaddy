package com.example.smartpaddy.presentation.loginPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpaddy.domain.usecase.auth.AuthUseCase
import kotlinx.coroutines.launch

class LoginViewModel (
  private val authUseCase: AuthUseCase
) : ViewModel() {

  private val _loginResult = MutableLiveData<Boolean>()
  val loginResult: LiveData<Boolean>
    get() = _loginResult


  fun loginUser(email: String, password: String) {
    viewModelScope.launch {
      try {
        authUseCase.loginUser(email, password)
        _loginResult.value = true
      } catch (e: Exception) {
        _loginResult.value = false
      }
    }
  }
}
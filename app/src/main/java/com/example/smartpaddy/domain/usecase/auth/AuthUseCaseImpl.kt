package com.example.smartpaddy.domain.usecase.auth

import com.example.smartpaddy.domain.repository.AuthRepository
import com.google.firebase.auth.AuthResult
import javax.inject.Inject

class AuthUseCaseImpl (
  private val authRepository: AuthRepository
) : AuthUseCase {

  override fun getCurrentUserID(): String {
    return authRepository.getCurrentUserID()
  }

  override suspend fun registerUser(email: String, password: String): AuthResult? {
    return authRepository.registerUser(email, password)
  }

  override suspend fun loginUser(email: String, password: String): AuthResult? {
    return authRepository.loginUser(email, password)
  }
}
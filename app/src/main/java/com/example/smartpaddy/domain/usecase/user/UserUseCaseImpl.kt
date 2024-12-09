package com.example.smartpaddy.domain.usecase.user

import com.example.smartpaddy.domain.model.User
import com.example.smartpaddy.domain.repository.AuthRepository
import com.example.smartpaddy.domain.repository.UserRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class UserUseCaseImpl (
  private val userRepository: UserRepository,
  private val authRepository: AuthRepository
) : UserUseCase {

  override suspend fun getCurrentUser(): StateFlow<User> {
    return userRepository.getCurrentUser(authRepository.getCurrentUserID())
  }

  override suspend fun storeUser(uid: String, user: User) {
    userRepository.storeUser(uid, user)
  }
}
package com.example.smartpaddy.domain.usecase.user

import com.example.smartpaddy.domain.model.User
import kotlinx.coroutines.flow.StateFlow

interface UserUseCase {
  suspend fun getCurrentUser(): StateFlow<User>
  suspend fun storeUser(uid: String, user: User)
}
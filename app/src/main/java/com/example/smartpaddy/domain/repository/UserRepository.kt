package com.example.smartpaddy.domain.repository

import com.example.smartpaddy.domain.model.User
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
  suspend fun getCurrentUser(uid: String): StateFlow<User>
  suspend fun storeUser(uid: String, user: User)
}
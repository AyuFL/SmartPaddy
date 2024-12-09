package com.example.smartpaddy.data.model

import com.example.smartpaddy.domain.model.User
import com.google.firebase.firestore.PropertyName

data class UserResponse(
  @get:PropertyName("user_id")
  @set:PropertyName("user_id")
  var id: String = "",

  @get:PropertyName("user_name")
  @set:PropertyName("user_name")
  var name: String = "",

  @get:PropertyName("user_email")
  @set:PropertyName("user_email")
  var email: String = "",
) {

  constructor() : this("", "", "")

  companion object {
    fun transform(userResponse: UserResponse): User {
      return User(
        id = userResponse.id,
        name = userResponse.name,
        email = userResponse.email
      )
    }

    fun transform(user: User): UserResponse {
      return UserResponse(
        id = user.id,
        name = user.name,
        email = user.email
      )
    }
  }
}
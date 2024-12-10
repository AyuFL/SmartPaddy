package com.example.smartpaddy.data.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
  @field:SerializedName("email")
  val email: String,

  @field:SerializedName("name")
  val name: String,

  @field:SerializedName("token")
  val token: String,
)

package com.example.smartpaddy.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
  @field:SerializedName("message")
  val message: String? = "",

  @field:SerializedName("status")
  val status: String,

  @field:SerializedName("user")
  val user: UserResponse
)
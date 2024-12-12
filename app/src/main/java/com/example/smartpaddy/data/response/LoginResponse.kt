package com.example.smartpaddy.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
  @field:SerializedName("message")
  val message: String?,

  @field:SerializedName("status")
  val status: String,

  @field:SerializedName("user_id")
  val token: String? = null,

  @field:SerializedName("name")
  val name: String? = null,

  @field:SerializedName("email")
  val email: String? = null,
)
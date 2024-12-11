package com.example.smartpaddy.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
  @field:SerializedName("message")
  val message: String?,

  @field:SerializedName("status")
  val status: String,

  @field:SerializedName("token")
  val token: String? = null,

  @field:SerializedName("name")
  val name: String? = null,
)
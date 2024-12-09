package com.example.smartpaddy.data.response

import com.google.gson.annotations.SerializedName

data class DataResponse(
  @field:SerializedName("predict_id")
  val predictId: String? = "",

  @field:SerializedName("user_id")
  val userId: String? = "",

  @field:SerializedName("result")
  val result: ResultResponse
)

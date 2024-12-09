package com.example.smartpaddy.data.response

import com.google.gson.annotations.SerializedName

data class HistoryResponse (
  @field:SerializedName("status")
  val status: String,

  @field:SerializedName("message")
  val message: String? = "",

  @field:SerializedName("data")
  val data: List<DataResponse>
)
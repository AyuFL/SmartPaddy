package com.example.smartpaddy.data.response

import com.google.gson.annotations.SerializedName

data class ResultResponse (
  @field:SerializedName("c_menangani")
  val caraMenangani: String,

  @field:SerializedName("gejala")
  val gejala: String,

  @field:SerializedName("message")
  val message: String? = "",

  @field:SerializedName("penjelasan")
  val penjelasan: String,

  @field:SerializedName("predicted_class")
  val predictedClass: String,

  @field:SerializedName("predicted_prob")
  val predictedProb: Float
)
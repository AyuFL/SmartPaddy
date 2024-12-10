package com.example.smartpaddy.data.retrofit

import com.example.smartpaddy.data.response.HistoryResponse
import com.example.smartpaddy.data.response.PostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
  @Multipart
  @POST("scan")
  suspend fun scan(
    @Part("imageUri") imageUri: MultipartBody.Part,
    @Part("userIds") userIds: RequestBody
  ): PostResponse

  @GET("post/{id}")
  suspend fun getDetailPost(
    @Path("predict_id") id: String
  ): PostResponse

  @GET("history/{user_id}")
  suspend fun getHistory(
    @Path("user_id") userId: String
  ): HistoryResponse
}
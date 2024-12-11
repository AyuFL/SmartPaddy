package com.example.smartpaddy.data.retrofit

import com.example.smartpaddy.data.request.LoginRequest
import com.example.smartpaddy.data.request.RegisterRequest
import com.example.smartpaddy.data.response.HistoryResponse
import com.example.smartpaddy.data.response.LoginResponse
import com.example.smartpaddy.data.response.PostResponse
import com.example.smartpaddy.data.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
  @Multipart
  @POST("scan")
  suspend fun scan(
    @Part imageUri: MultipartBody.Part,
    @Part("userIds") token: RequestBody
  ): PostResponse

  @GET("post/{predict_id}")
  suspend fun getDetailPost(
    @Path("predict_id") predictId: String
  ): PostResponse

  @GET("history/{user_id}")
  suspend fun getHistory(
    @Path("user_id") token: String
  ): HistoryResponse

  @POST("register")
  suspend fun register(
    @Body registerRequest: RegisterRequest
  ): RegisterResponse

  @POST("login")
  suspend fun login(
    @Body loginRequest: LoginRequest
  ): LoginResponse
}
package com.example.smartpaddy.data.retrofit

import com.example.smartpaddy.data.response.HistoryResponse
import com.example.smartpaddy.data.response.LoginResponse
import com.example.smartpaddy.data.response.PostResponse
import com.example.smartpaddy.data.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
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

  @GET("history/{id}")
  suspend fun getHistory(
    @Path("user_id") id: String
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
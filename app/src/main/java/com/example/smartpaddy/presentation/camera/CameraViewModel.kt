package com.example.smartpaddy.presentation.camera

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpaddy.data.response.PostResponse
import com.example.smartpaddy.data.retrofit.ApiConfig
import com.example.smartpaddy.utils.parseErrorResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class CameraViewModel : ViewModel() {

  var currentImageUri: Uri? = null

  private val _message = MutableLiveData<String>()
  val message: LiveData<String> = _message

  fun analyzeImage(token: RequestBody, imageFile: MultipartBody.Part) {
    viewModelScope.launch {
      try {
        val apiService = ApiConfig.getApiService()
        val response = apiService.scan(imageFile, token)

        _message.value = response.message?.takeIf { it.isNotEmpty() } ?: "Success!"
      } catch (e: Exception) {
        var error = PostResponse()

        if (e is HttpException) {
          val errorBody = e.response()?.errorBody()?.string()
          val apiResponse = parseErrorResponse(errorBody)

          error = PostResponse(
            status = e.code().toString(),
            message = apiResponse?.message ?: "Unknown error"
          )
        }

        _message.value = error.message?.takeIf { it.isNotEmpty() } ?: "An unexpected error occurred"
      }
    }
  }
}
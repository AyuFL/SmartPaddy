package com.example.smartpaddy.presentation.camera

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpaddy.databinding.ActivityCameraBinding
import com.example.smartpaddy.utils.Constants
import com.example.smartpaddy.utils.getImageUri
import com.example.smartpaddy.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class CameraActivity : AppCompatActivity() {

  private lateinit var binding: ActivityCameraBinding
  private val viewModel: CameraViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityCameraBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.apply {
      galleryBtn.setOnClickListener { startGallery() }
      cameraBtn.setOnClickListener { startCamera() }
      analyzeBtn.setOnClickListener { analyzeImage() }
      btnBack.setOnClickListener { finish() }
    }

    viewModel.message.observe(this) {
      showToast(this, it)
    }
  }

  private fun startGallery() {
    launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
  }

  private val launcherGallery = registerForActivityResult(
    ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    if (uri != null) {
      viewModel.currentImageUri = uri
      showImage()
    }
  }

  private fun startCamera() {
    viewModel.currentImageUri = getImageUri(this)
    launcherIntentCamera.launch(viewModel.currentImageUri!!)
  }

  private val launcherIntentCamera = registerForActivityResult(
    ActivityResultContracts.TakePicture()
  ) { isSuccess ->
    if (isSuccess) {
      showImage()
    } else {
      viewModel.currentImageUri = null
    }
  }

  private fun showImage() {
    viewModel.currentImageUri?.let {
      binding.previewIv.setImageURI(it)
    }
  }

  private fun analyzeImage() {
    viewModel.currentImageUri?.let { uri ->
      val imageFile = uriToFile(uri, this)

      val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
      val multipartBody = MultipartBody.Part.createFormData(
        "imageUri",
        imageFile.name,
        requestImageFile
      )

      val token = getToken()?.toRequestBody("text/plain".toMediaType())

      showLoading(true)

      if (token != null) {
        viewModel.analyzeImage(token, multipartBody)
      } else {
        showToast(this, "Token is null. Failed to analyze")
      }
    } ?: run {
      showToast(this, "Please select an image first!")
    }

    showLoading(false)
  }

  private fun showLoading(isLoading: Boolean) {
    // binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
  }

  fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
  }

  private fun getToken(): String? {
    val sharedPreferences = getSharedPreferences(Constants.login, MODE_PRIVATE)
    return sharedPreferences.getString(Constants.token, null)
  }
}

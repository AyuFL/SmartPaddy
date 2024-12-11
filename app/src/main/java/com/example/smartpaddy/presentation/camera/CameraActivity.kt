package com.example.smartpaddy.presentation.camera

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

  private val CAMERA_PERMISSION_REQUEST_CODE = 100

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityCameraBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.apply {
      galleryBtn.setOnClickListener { startGallery() }
      cameraBtn.setOnClickListener { checkAndRequestCameraPermission() }
      analyzeBtn.setOnClickListener { analyzeImage() }
      btnBack.setOnClickListener { finish() }
    }

    viewModel.message.observe(this) {
      showToast(this, it)
    }
  }

  private fun checkAndRequestCameraPermission() {
    if (ContextCompat.checkSelfPermission(
        this, android.Manifest.permission.CAMERA
      ) == PackageManager.PERMISSION_GRANTED
    ) {
      startCamera()
    } else {
      ActivityCompat.requestPermissions(
        this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE
      )
    }
  }

  override fun onRequestPermissionsResult(
    requestCode: Int, permissions: Array<String>, grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
      if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        startCamera()
      } else {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            android.Manifest.permission.CAMERA
          )
        ) {
          showToast(this, "Camera permission is required to take a picture.")

          ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE
          )
        } else {
          showToast(this, "Camera permission is required. Please enable it in the app settings.")
          openAppSettings()
        }
      }
    }
  }

  private fun openAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivity(intent)
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
    binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    binding.analyzeBtn.visibility = if (isLoading) View.GONE else View.VISIBLE
  }

  fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
  }

  private fun getToken(): String? {
    val sharedPreferences = getSharedPreferences(Constants.login, MODE_PRIVATE)
    return sharedPreferences.getString(Constants.token, null)
  }
}
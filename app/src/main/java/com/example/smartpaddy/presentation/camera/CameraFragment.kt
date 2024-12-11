package com.example.smartpaddy.presentation.camera

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.smartpaddy.databinding.FragmentCameraBinding
import com.example.smartpaddy.utils.Constants
import com.example.smartpaddy.utils.getImageUri
import com.example.smartpaddy.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class CameraFragment : Fragment() {

  private lateinit var binding: FragmentCameraBinding
  private val viewModel: CameraViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentCameraBinding.inflate(layoutInflater)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.galleryBtn.setOnClickListener { startGallery() }

<<<<<<< HEAD
    binding.analyzeBtn.setOnClickListener { startCamera() }
=======
    binding.cameraBtn.setOnClickListener { startCamera() }
>>>>>>> refs/heads/md-bella

    binding.analyzeBtn.setOnClickListener { analyzeImage() }

    viewModel.message.observe(viewLifecycleOwner) {
      showToast(requireContext(), it)
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
    viewModel.currentImageUri = getImageUri(requireContext())
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
      val imageFile = uriToFile(uri, requireContext())

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
        showToast(requireContext(), "Token is null. Failed to analyze")
      }
    } ?: run {
      showToast(requireContext(), "Please select an image first!")
    }

    showLoading(false)
  }

  private fun showLoading(isLoading: Boolean) {
//    binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
  }

  fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
  }

  private fun getToken(): String? {
    val sharedPreferences = requireContext().getSharedPreferences(Constants.login, MODE_PRIVATE)
    return sharedPreferences.getString(Constants.token, null)
  }
}
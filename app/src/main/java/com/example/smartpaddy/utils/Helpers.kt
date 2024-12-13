package com.example.smartpaddy.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.example.smartpaddy.BuildConfig
import com.example.smartpaddy.data.response.PostResponse
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

fun uriToFile(imageUri: Uri, context: Context): File {
  val myFile = createCustomTempFile(context)
  val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
  val outputStream = FileOutputStream(myFile)
  val buffer = ByteArray(1024)
  var length: Int
  while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
  outputStream.close()
  inputStream.close()
  return myFile
}

fun createCustomTempFile(context: Context): File {
  val filesDir = context.externalCacheDir ?: context.cacheDir
  return File.createTempFile(timeStamp, ".jpg", filesDir)
}

fun getImageUri(context: Context): Uri {
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    getImageUriForQAndAbove(context)
  } else {
    getImageUriForBelowQ(context)
  }
}

private fun getImageUriForQAndAbove(context: Context): Uri {
  val contentValues = ContentValues().apply {
    put(MediaStore.MediaColumns.DISPLAY_NAME, "$timeStamp.jpg")
    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
    put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCamera/")
  }
  return context.contentResolver.insert(
    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
    contentValues
  ) ?: throw IllegalStateException("Failed to create image URI")
}

private fun getImageUriForBelowQ(context: Context): Uri {
  val picturesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
  val imageDir = File(picturesDir, "MyCamera")
  if (!imageDir.exists()) {
    imageDir.mkdirs() // Ensure the directory exists
  }
  val imageFile = File(imageDir, "$timeStamp.jpg")
  return FileProvider.getUriForFile(
    context,
    "${BuildConfig.APPLICATION_ID}.fileprovider",
    imageFile
  )
}

fun parseErrorResponse(errorBody: String?): PostResponse? {
  return try {
    errorBody?.let {
      val gson = Gson()
      gson.fromJson(it, PostResponse::class.java)
    }
  } catch (e: Exception) {
    null
  }
}
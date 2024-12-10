package com.example.smartpaddy.presentation.registerPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpaddy.R
import com.example.smartpaddy.databinding.ActivityRegisterBinding
import com.example.smartpaddy.presentation.loginPage.LoginActivity
import com.example.smartpaddy.utils.Constants

class RegisterActivity : AppCompatActivity() {

  private lateinit var binding: ActivityRegisterBinding
  private val passwordPatterns = Constants.passwordPatterns
  private var registerClicked = false
  private val viewModel: RegisterViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityRegisterBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.btnRegister.setOnClickListener {
      if (!registerClicked) {
        registerClicked = true
        validateAndRegister()
      }
    }

    binding.tvLogin.setOnClickListener {
      goToLoginPage()
    }

    observeViewModel()
  }

  private fun observeViewModel() {
    viewModel.registerResponse.observe(this) { response ->
      if (response.status == "success") {
        saveUserDetailsToSharedPreferences(
          response.user.token,
          response.user.name,
          response.user.email
        )
        Toast.makeText(this, getString(R.string.registration_success), Toast.LENGTH_SHORT).show()
        goToLoginPage()
      } else {
        Toast.makeText(
          this,
          response.message ?: getString(R.string.registration_failed),
          Toast.LENGTH_SHORT
        ).show()
        registerClicked = false
      }
    }
  }

  private fun saveUserDetailsToSharedPreferences(token: String, name: String, email: String) {
    val sharedPreferences = getSharedPreferences(Constants.login, MODE_PRIVATE)
    with(sharedPreferences.edit()) {
      putString(Constants.token, token)
      putString(Constants.name, name)
      putString(Constants.email, email)
      apply()
    }
    Log.d("RegisterActivity", "Saved Token: $token")
    Log.d("RegisterActivity", "Saved User Name: $name")
    Log.d("RegisterActivity", "Saved Email: $email")
  }

  private fun validateAndRegister() {
    val name = binding.etName.text.toString()
    val email = binding.etEmail.text.toString()
    val password = binding.etPassword.text.toString()

    when {
      name.isEmpty() || email.isEmpty() || password.isEmpty() -> {
        showValidationError(getString(R.string.email_pass_empty))
      }

      !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
        showValidationError(getString(R.string.email_not_valid))
      }

      !passwordPatterns.matcher(password).matches() -> {
        showValidationError(getString(R.string.password_not_valid))
      }

      else -> {
        viewModel.register(name, email, password)
        return
      }
    }
    registerClicked = false
  }

  private fun showValidationError(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
  }

  private fun goToLoginPage() {
    startActivity(Intent(this, LoginActivity::class.java))
    finish()
  }


}

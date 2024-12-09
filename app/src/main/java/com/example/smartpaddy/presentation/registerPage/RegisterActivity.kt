package com.example.smartpaddy.presentation.registerPage

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpaddy.R
import com.example.smartpaddy.databinding.ActivityRegisterBinding
import com.example.smartpaddy.presentation.loginPage.LoginActivity
import com.example.smartpaddy.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

class RegisterActivity : AppCompatActivity() {

  private lateinit var binding: ActivityRegisterBinding
  private val viewModel: RegisterViewModel by viewModels()
  private val passwordPatterns = Constants.passwordPatterns
  private var registerClicked = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityRegisterBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.btnRegister.setOnClickListener {
      if (!registerClicked) {
        registerClicked = true
        storeAndValidation()
      }
    }

    binding.tvLogin.setOnClickListener {
      goToLoginPage()
    }
  }

  private fun storeAndValidation() {
    val email = binding.etEmail.text.toString()
    val password = binding.etPassword.text.toString()
    val name = binding.etName.text.toString()

    if (email.isEmpty() || password.isEmpty()) {
      showValidationError(getString(R.string.email_pass_empty))
      registerClicked = false
      return
    }

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      showValidationError(getString(R.string.email_not_valid))
      registerClicked = false
      return
    }

    if (!passwordPatterns.matcher(password).matches()) {
      showValidationError(getString(R.string.password_not_valid))
      registerClicked = false
      return
    }

    viewModel.registerUser(email, name, password) { success ->
      if (success) {
        Toast.makeText(this, R.string.register_success, Toast.LENGTH_SHORT).show()
        goToLoginPage()
      } else {
        Toast.makeText(this, R.string.register_failed, Toast.LENGTH_SHORT).show()
        registerClicked = false
      }
    }
  }

  private fun showValidationError(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
  }

  private fun goToLoginPage() {
    startActivity(Intent(this, LoginActivity::class.java))
    finish()
  }
}
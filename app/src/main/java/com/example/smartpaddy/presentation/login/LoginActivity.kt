package com.example.smartpaddy.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpaddy.R
import com.example.smartpaddy.databinding.ActivityLoginBinding
import com.example.smartpaddy.presentation.loginPage.LoginViewModel
import com.example.smartpaddy.presentation.main.MainActivity
import com.example.smartpaddy.presentation.register.RegisterActivity
import com.example.smartpaddy.utils.Constants

class LoginActivity : AppCompatActivity() {

  private lateinit var binding: ActivityLoginBinding
  private val viewModel: LoginViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityLoginBinding.inflate(layoutInflater)
    setContentView(binding.root)

    observeLoginResponse()

    if (isLoggedIn()) {
      navigateToMainActivity()
      return
    }

    binding.btnLogin.setOnClickListener {
      validateLogin()
    }

    binding.tvRegister.setOnClickListener {
      goToRegisterPage(it)
    }
  }

  private fun observeLoginResponse() {
    viewModel.loginResponse.observe(this) { response ->
      showLoading(false)
      if (response.status == "success" && !response.token.isNullOrEmpty() && !response.name.isNullOrEmpty() && !response.email.isNullOrEmpty()) {
        saveLoginDetailsToSharedPreferences(
          response.token,
          response.name,
          response.email
        )
        navigateToMainActivity()
      } else {
        Toast.makeText(
          this,
          response.message ?: getString(R.string.login_failed),
          Toast.LENGTH_SHORT
        ).show()
      }
    }
  }

  private fun saveLoginDetailsToSharedPreferences(token: String, name: String, email: String) {
    val sharedPreferences = getSharedPreferences(Constants.login, MODE_PRIVATE)
    with(sharedPreferences.edit()) {
      putString(Constants.token, token)
      putBoolean(Constants.login, true)
      putString(Constants.name, name)
      putString(Constants.email, email)
      apply()
    }
    Log.d("LoginActivity", "Saved Token: $token, Name: $name")
  }

  private fun validateLogin() {
    val email = binding.etEmail.text.toString()
    val password = binding.etPassword.text.toString()

    if (email.isEmpty() || password.isEmpty()) {
      Toast.makeText(this, R.string.email_pass_empty, Toast.LENGTH_SHORT).show()
      return
    }

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      Toast.makeText(this, R.string.email_not_valid, Toast.LENGTH_SHORT).show()
      return
    }

    showLoading(true)
    viewModel.login(email, password)
  }

  private fun goToRegisterPage(view: View) {
    val intent = Intent(view.context.applicationContext, RegisterActivity::class.java)
    startActivity(intent)
  }

  private fun navigateToMainActivity() {
    val intent = Intent(this, MainActivity::class.java)
    startActivity(intent)
    finish()
  }

  private fun isLoggedIn(): Boolean {
    val sharedPreferences = getSharedPreferences(Constants.login, MODE_PRIVATE)
    return sharedPreferences.getBoolean(Constants.login, false)
  }

  private fun showLoading(isLoading: Boolean) {
    binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
  }
}

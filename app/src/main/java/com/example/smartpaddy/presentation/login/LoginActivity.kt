package com.example.smartpaddy.presentation.login

import android.content.Intent
import android.os.Bundle
<<<<<<< HEAD
=======
import android.util.Log
>>>>>>> refs/heads/md-bella
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpaddy.R
import com.example.smartpaddy.databinding.ActivityLoginBinding
import com.example.smartpaddy.presentation.main.MainActivity
import com.example.smartpaddy.presentation.register.RegisterActivity
import com.example.smartpaddy.utils.Constants
<<<<<<< HEAD
=======
import com.example.smartpaddy.utils.Constants.name
>>>>>>> refs/heads/md-bella

class LoginActivity : AppCompatActivity() {

  private lateinit var binding: ActivityLoginBinding
  private val viewModel: LoginViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityLoginBinding.inflate(layoutInflater)
    setContentView(binding.root)

<<<<<<< HEAD
=======
    observeLoginResponse()

>>>>>>> refs/heads/md-bella
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
<<<<<<< HEAD

    viewModel.loginResponse.observe(this) { response ->
      if (response.status == "success") {
        response.token?.let { saveLoginState(it, response.message ?: "User") }

        Toast.makeText(this, "Welcome ${response.message}", Toast.LENGTH_SHORT).show()

        navigateToMainActivity()
      } else {
        Toast.makeText(this, response.message ?: "Login failed", Toast.LENGTH_SHORT).show()
=======
  }

  private fun observeLoginResponse() {
    viewModel.loginResponse.observe(this) { response ->
      if (response.status == "success" && !response.token.isNullOrEmpty() && !response.name.isNullOrEmpty()) {
        saveLoginDetailsToSharedPreferences(response.token, response.name)
        navigateToMainActivity()
      } else {
        Toast.makeText(
          this,
          response.message ?: getString(R.string.login_failed),
          Toast.LENGTH_SHORT
        ).show()
>>>>>>> refs/heads/md-bella
      }
    }
  }

<<<<<<< HEAD
  private fun saveLoginState(token: String, userName: String) {
    val sharedPreferences = getSharedPreferences(Constants.login, MODE_PRIVATE)
    with(sharedPreferences.edit()) {
      putBoolean(Constants.isLoggedIn, true)
      putString(Constants.userName, userName)
      putString(Constants.token, token)
      apply()
    }
  }

=======
  private fun saveLoginDetailsToSharedPreferences(token: String, name: String) {
    val sharedPreferences = getSharedPreferences(Constants.login, MODE_PRIVATE)
    with(sharedPreferences.edit()) {
      putString(Constants.token, token)
      putBoolean(Constants.login, true)
      putString(Constants.name, name)
      apply()
    }
    Log.d("LoginActivity", "Saved Token: $token, Name: $name")
  }


>>>>>>> refs/heads/md-bella
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
<<<<<<< HEAD
    return sharedPreferences.getBoolean(Constants.isLoggedIn, false)
=======
    return sharedPreferences.getBoolean(Constants.login, false)
>>>>>>> refs/heads/md-bella
  }
}


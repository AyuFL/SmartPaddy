package com.example.smartpaddy.presentation.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.InputType
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


    playAnimation()
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

    binding.cbShowPassword.setOnCheckedChangeListener { _, isChecked ->
      if (isChecked) {
        binding.etPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
      } else {
        binding.etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
      }
      binding.etPassword.text?.let { binding.etPassword.setSelection(it.length) }
    }
  }

  private fun playAnimation() {
    val title = ObjectAnimator.ofFloat(binding.tvLoginTitle, View.ALPHA, 1f).setDuration(700)
    val emailTitle = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(700)
    val email = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(700)
    val passwordTitle =
      ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(700)
    val password =
      ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(700)
    val loginButton = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(700)
    val register = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(700)
    val showPassword = ObjectAnimator.ofFloat(binding.cbShowPassword, View.ALPHA, 1f).setDuration(700)

    AnimatorSet().apply {
      playSequentially(title, emailTitle, email, passwordTitle, password, showPassword, loginButton, register)
      start()
    }
  }

  private fun observeLoginResponse() {
    viewModel.loginResponse.observe(this) { response ->
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
    val password = binding.etPassword.getPassword()

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
    return sharedPreferences.getBoolean(Constants.login, false)
  }
}

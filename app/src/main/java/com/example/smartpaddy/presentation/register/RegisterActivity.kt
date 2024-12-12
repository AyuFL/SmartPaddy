package com.example.smartpaddy.presentation.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpaddy.R
import com.example.smartpaddy.databinding.ActivityRegisterBinding
import com.example.smartpaddy.presentation.login.LoginActivity
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

    playAnimation()

    binding.etName.setOnEditorActionListener { _, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        binding.etEmail.requestFocus()
        true
      } else {
        false
      }
    }

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

  private fun playAnimation() {
    val title = ObjectAnimator.ofFloat(binding.tvRegisterTitle, View.ALPHA, 1f).setDuration(700)
    val nameTitle = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(700)
    val name = ObjectAnimator.ofFloat(binding.etName, View.ALPHA, 1f).setDuration(700)
    val emailTitle = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(700)
    val email = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(700)
    val passwordTitle =
      ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(700)
    val password =
      ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(700)
    val registerButton = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(700)
    val loginButton = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(700)

    AnimatorSet().apply {
      playSequentially(title, nameTitle, name, emailTitle, email, passwordTitle, password, registerButton, loginButton)
      start()
    }
  }

  private fun observeViewModel() {
    viewModel.registerResponse.observe(this) { response ->
      showLoading(false)

      if (response.status == "success") {
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

  private fun validateAndRegister() {
    val name = binding.etName.text.toString().trim()
    val email = binding.etEmail.text.toString().trim()
    val password = binding.etPassword.getPassword()

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
        showLoading(true)
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

  private fun showLoading(isLoading: Boolean) {
    binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
  }
}

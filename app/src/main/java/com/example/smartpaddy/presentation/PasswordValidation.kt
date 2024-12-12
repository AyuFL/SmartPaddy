package com.example.smartpaddy.presentation

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.example.smartpaddy.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class PasswordValidation @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = android.R.attr.editTextStyle,
) : TextInputEditText(context, attrs, defStyleAttr) {

  private var parentTextInputLayout: TextInputLayout? = null

  init {
    setupValidation()
  }

  private fun setupValidation() {
    post {
      parentTextInputLayout = parent as? TextInputLayout
    }
    addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        validatePassword(s.toString())
      }

      override fun afterTextChanged(s: Editable?) {}
    })
  }

  private fun validatePassword(password: String) {
    error = if (password.length < 8) {
      context.getString(R.string.error)
    } else {
      null
    }
  }

  fun getPassword(): String {
    return text.toString()
  }
}
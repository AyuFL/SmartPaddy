package com.example.smartpaddy.utils

import java.util.regex.Pattern

object Constants {
  val passwordPatterns: Pattern =
    Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")
  const val login = "login_preferences"
  const val isLoggedIn = "is_logged_in"
  const val userName = "user_name"
  const val token = "token"
}
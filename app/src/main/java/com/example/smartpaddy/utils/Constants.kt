package com.example.smartpaddy.utils

import java.util.regex.Pattern

object Constants {
  val passwordPatterns: Pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")
  const val login = "login"
  const val isLoggedIn = "isLoggedIn"
}
package com.example.smartpaddy.presentation.mainPage

import androidx.lifecycle.ViewModel
import com.example.smartpaddy.domain.usecase.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class MainActivityViewModel (
  private val userUseCase: UserUseCase
) : ViewModel()
package com.example.smartpaddy.presentation.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.smartpaddy.utils.SettingPreferences
import com.example.smartpaddy.utils.dataStore
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
  private val pref: SettingPreferences = SettingPreferences.getInstance(application.dataStore)

  fun getThemeSettings(): LiveData<Boolean> {
    return pref.getThemeSetting().asLiveData()
  }

  fun saveThemeSetting(isDarkModeActive: Boolean) {
    viewModelScope.launch {
      pref.saveThemeSetting(isDarkModeActive)
    }
  }
}
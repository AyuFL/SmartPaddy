package com.example.smartpaddy.ui.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.smartpaddy.databinding.FragmentCameraBinding

class CameraFragment : Fragment() {

  private lateinit var binding: FragmentCameraBinding

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentCameraBinding.inflate(layoutInflater)
    return binding.root
  }
}
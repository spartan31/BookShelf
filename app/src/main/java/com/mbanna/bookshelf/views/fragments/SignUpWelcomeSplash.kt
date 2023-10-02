package com.mbanna.bookshelf.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mbanna.bookshelf.views.activity.LoginScreen
import com.mbanna.bookshelf.databinding.FragmentSignUpWelcomeSplashBinding

class SignUpWelcomeSplash : Fragment() {
    private lateinit var binding: FragmentSignUpWelcomeSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignUpWelcomeSplashBinding.inflate(inflater, container, false)

        onButtonClick()
        return binding.root
    }

    private fun onButtonClick() {
        binding.homeButton.setOnClickListener {
            Intent(activity, LoginScreen::class.java).also {
                activity?.finish()
                startActivity(it)
            }
        }
    }
}
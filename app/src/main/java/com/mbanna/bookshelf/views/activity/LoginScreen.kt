package com.mbanna.bookshelf.views.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mbanna.bookshelf.R
import com.mbanna.bookshelf.databinding.ActivityLoginScreenBinding
import com.mbanna.bookshelf.di.Provider
import com.mbanna.bookshelf.viewModel.LoginViewModel
import com.mbanna.bookshelf.viewModel.factory.LoginViewModelFactory
import com.mbanna.bookshelf.views.fragments.SignUpFragment

class LoginScreen : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityLoginScreenBinding
    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_screen)

        val userRepository = Provider.getInstance(this).getUserRepository()
        viewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(userRepository)
        )[LoginViewModel::class.java]
        binding.logInModel = viewModel
        binding.lifecycleOwner = this



        try {
            attachListeners()
            startObserving()

        } catch (e: Exception) {

        }
    }

    private fun navigateToHome() {
        val pref = getSharedPreferences("login", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean("flag", true)
        editor.apply()
        val view: View? = this.currentFocus

        if (view != null) {
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
        val intent = Intent(this, BookShelf::class.java)
        finish()
        startActivity(intent)
    }

    private fun addFragment(fragment: Fragment) {
        binding.inputUsername.error = null
        binding.inputPassword.error = null
        val view: View? = this.currentFocus

        if (view != null) {
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, fragment)
            addToBackStack("login")
            commit()
        }
    }

    fun startObserving(){
        viewModel.userNotAvailable.observe(this) {
            if (it == true) {
                binding.inputUsername.error = "User not exists. Please signup"
                binding.inputUsername.isEnabled = true
            }
        }
        viewModel.inValidPassword.observe(this) {
            if (it == true) {
                binding.inputPassword.error = "Wrong Password"
                binding.inputPassword.isEnabled = true
            }
        }

        viewModel.emptyUsername.observe(this) {
            if (it == true) {
                binding.inputUsername.error = "Please enter this field"
                binding.inputUsername.isEnabled = true
            }
        }
        viewModel.emptyPassword.observe(this) {
            if (it == true) {
                binding.inputPassword.error = "Please enter this field"
                binding.inputPassword.isEnabled = true
            }
        }

        viewModel.navigateToHomeScreen.observe(this){
            if (it == true){
                navigateToHome()
            }
        }
    }


    override fun onClick(p: View?) {
        when (p) {
            binding.signup -> {
                addFragment(SignUpFragment())
            }

            binding.loginBtn -> {
                viewModel.perFormLogin(id = binding.inputUsername.text.toString(), password = binding.inputPassword.text.toString())
            }
        }
    }

    private fun attachListeners() {
        binding.signup.setOnClickListener(this)
        binding.loginBtn.setOnClickListener(this)
    }

}
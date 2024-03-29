package com.mbanna.bookshelf.views.activity

import android.content.Intent
import android.os.Bundle
import android.text.InputType
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
        startActivity(intent)
        finish()
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
                binding.inputUsername.error = resources.getString(R.string.user_not_exist)
                binding.inputUsername.isEnabled = true
            }
        }
        viewModel.inValidPassword.observe(this) {
            if (it == true) {
                binding.inputPassword.error = resources.getString(R.string.wrong_password)
                binding.inputPassword.isEnabled = true
            }
        }

        viewModel.emptyUsername.observe(this) {
            if (it == true) {
                binding.inputUsername.error = resources.getString(R.string.enter_field)
                binding.inputUsername.isEnabled = true
            }
        }
        viewModel.emptyPassword.observe(this) {
            if (it == true) {
                binding.inputPassword.error = resources.getString(R.string.enter_field)
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

            binding.tvShowPass ->{
                if (viewModel.passwordVisibility.value != true){
                     binding.inputPassword.inputType =  InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    binding.tvShowPass.text = resources.getString(R.string.hide_password)
                    viewModel.passwordVisibility.value = true
                }else{
                    binding.inputPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    binding.tvShowPass.text = resources.getString(R.string.show_password)
                    viewModel.passwordVisibility.value = false
                }
            }
        }
    }

    private fun attachListeners() {
        binding.signup.setOnClickListener(this)
        binding.loginBtn.setOnClickListener(this)
        binding.tvShowPass.setOnClickListener(this)
    }

}
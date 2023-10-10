package com.mbanna.bookshelf.views.fragments

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mbanna.bookshelf.R
import com.mbanna.bookshelf.databinding.FragmentSignUpBinding
import com.mbanna.bookshelf.di.Provider
import com.mbanna.bookshelf.utils.JsonParser
import com.mbanna.bookshelf.viewModel.SignUpViewModel
import com.mbanna.bookshelf.viewModel.factory.SignUpViewFactory
import com.mbanna.bookshelf.views.activity.BookShelf

class SignUpFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentSignUpBinding
    lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val userRepository = Provider.getInstance(context).getUserRepository()
        viewModel =
            ViewModelProvider(this, SignUpViewFactory(userRepository))[SignUpViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        try {
            setUp()
            observeFields()
            clickListener()
        } catch (e: Exception) {
            Log.i("SignUp", e.printStackTrace().toString())
        }
        return binding.root
    }

    private fun setUp() {
        val countries = JsonParser.readCountriesFromJson(requireContext())
        viewModel.countryList.addAll(countries)
        val spinner = binding.countrySpinner
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, countries)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun clickListener() {
        binding.signupButton.setOnClickListener(this)
        binding.tvShowPass.setOnClickListener(this)
        binding.tvShowConfirmPass.setOnClickListener(this)
    }


    private fun observeFields() {
        viewModel.emptyUsername.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.inputName.error = resources.getString(R.string.enter_field)
                binding.inputName.requestFocus()
            }
        }
        viewModel.emptyPassword.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.inputPassword.error = resources.getString(R.string.enter_field)
                binding.inputPassword.requestFocus()
            }
        }
        viewModel.emptyConfirmPass.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.confirmInputPassword.error = resources.getString(R.string.enter_field)
                binding.confirmInputPassword.requestFocus()
            }
        }

        viewModel.weakPassword.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.inputPassword.error = viewModel.passwordErrorMessage
                binding.inputPassword.requestFocus()
            }
        }

        viewModel.userAlreadyAvailable.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.inputName.error = resources.getString(R.string.user_already_exist)
                binding.inputName.requestFocus()
            }
        }
        viewModel.inValidPassword.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.confirmInputPassword.error = resources.getString(R.string.password_not_match)
                binding.confirmInputPassword.requestFocus()
            }
        }
        viewModel.countyNotSelected.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.countrySpinner.prompt =  resources.getString(R.string.choose_country)
            }
        }

        viewModel.navigateToSignupsSplashScreen.observe(viewLifecycleOwner) {
            if (it == true) {
               navigateToHome()
            }
        }

    }

    private fun navigateToHome() {
        val pref = activity?.getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
        val editor = pref?.edit()
        editor?.putBoolean("flag", true)
        editor?.apply()
        val view: View? = activity?.currentFocus
        binding.inputName.error = null
        binding.inputPassword.error = null
        binding.confirmInputPassword.error = null
        if (view != null) {
            val inputMethodManager =
                activity?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
        val intent = Intent(activity, BookShelf::class.java)
        startActivity(intent)

        activity?.finish()
    }


    override fun onClick(p0: View?) {
        when (p0) {
            binding.signupButton -> viewModel.validateData()

            binding.tvShowPass -> {
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

            binding.tvShowConfirmPass -> {
                if (viewModel.confirmPasswordVisibility.value != true){
                    binding.confirmInputPassword.inputType =  InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    binding.tvShowConfirmPass.text = resources.getString(R.string.hide_password)
                    viewModel.confirmPasswordVisibility.value = true
                }else{
                    binding.confirmInputPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    binding.tvShowConfirmPass.text = resources.getString(R.string.show_password)
                    viewModel.confirmPasswordVisibility.value = false
                }
            }
        }
    }

}
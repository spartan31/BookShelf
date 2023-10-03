package com.mbanna.bookshelf.views.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mbanna.bookshelf.R
import com.mbanna.bookshelf.databinding.FragmentSignUpBinding
import com.mbanna.bookshelf.di.Provider
import com.mbanna.bookshelf.utils.JsonParser
import com.mbanna.bookshelf.viewModel.SignUpViewModel
import com.mbanna.bookshelf.viewModel.factory.SignUpViewFactory

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
                binding.inputPassword.error = resources.getString(R.string.strong_password)
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
                launchSplash()
            }
        }

    }

    private fun launchSplash() {
        val view: View? = activity?.currentFocus

        if (view != null) {
            val inputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
        binding.inputName.error = null
        binding.inputPassword.error = null
        binding.confirmInputPassword.error = null
        val fragment = SignUpWelcomeSplash()
        val fragManager = requireActivity().supportFragmentManager
        fragManager.popBackStackImmediate()
        fragManager.beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null)
            .commit()
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.signupButton -> viewModel.validateData()
        }
    }

}
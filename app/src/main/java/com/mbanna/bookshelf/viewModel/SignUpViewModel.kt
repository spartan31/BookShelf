package com.mbanna.bookshelf.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbanna.bookshelf.model.repository.UserRepository
import com.mbanna.bookshelf.model.roomdatabase.User
import com.mbanna.bookshelf.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class SignUpViewModel(private val userRepo: UserRepository) : ViewModel() {

    var inputUsername = MutableLiveData<String>()
    var inputPassword = MutableLiveData<String>()
    var inputConfirmPassword = MutableLiveData<String>()
    var selectedCountryLiveData = MutableLiveData<Int>()
    var countryList = ArrayList<String>() ;

    var userAlreadyAvailable = MutableLiveData<Boolean>()
    var navigateToSignupsSplashScreen = MutableLiveData<Boolean>()
    var inValidPassword = MutableLiveData<Boolean>()
    var weakPassword = MutableLiveData<Boolean>()
    var emptyUsername = MutableLiveData<Boolean>()
    var emptyPassword = MutableLiveData<Boolean>()
    var emptyConfirmPass = MutableLiveData<Boolean>()
    var countyNotSelected = MutableLiveData<Boolean>()



    private fun insertUser(user: User): Boolean {
        var isSuccess = false

        runBlocking {
            val job = viewModelScope.async(Dispatchers.IO) {
                val boolean = userRepo.insertUser(user)
                boolean
            }
            isSuccess = job.await()
        }
        navigateToSignupsSplashScreen.value = true
        return isSuccess
    }

    private suspend fun getUserDetails(id: String): User? {
        return userRepo.getUserDetails(id)
    }


    private fun checkUniqueUserId(id: String): Int {
        var result = 0

        runBlocking {
                val job = viewModelScope.async(Dispatchers.IO) {
                    val user = getUserDetails(id)
                    Log.i("Sign UP ", "${user?.user_Name} and ${user?.user_Password}")
                    if (user != null) {
                        Constants.USER_ALREADY_EXIST
                    } else {
                        Constants.STATUS_OK
                    }
                }
                result = job.await()
            }
        return result
    }


    fun validateData() {
        if(!checkValidFields()){
            return
        }

        val user = User(user_Name = inputUsername.value!! , user_Password = inputPassword.value!! , user_Country = countryList[selectedCountryLiveData.value!!])
        insertUser(user)
    }

    private fun checkValidFields(): Boolean {
        return if (inputUsername.value == null || inputUsername.value!!.isEmpty()) {
            emptyUsername.value = true
            false
        } else if (checkUniqueUserId(inputUsername.value!!) != Constants.STATUS_OK) {
            userAlreadyAvailable.value = true
            false
        } else if (inputPassword.value == null || !isValidPassword(inputPassword.value!!)) {
            weakPassword.value = true
            false
        } else if (inputConfirmPassword.value == null || inputConfirmPassword.value!!.isEmpty() ||  inputPassword.value != inputConfirmPassword.value) {
            inValidPassword.value = true
            false
        }else {
            true
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val pattern = "^(?=.*[0-9])(?=.*[!@#\$%&()])(?=.*[a-z])(?=.*[A-Z]).{8,}$".toRegex()
        return pattern.matches(password)
    }

}
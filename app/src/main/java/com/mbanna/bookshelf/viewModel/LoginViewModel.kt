package com.mbanna.bookshelf.viewModel

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbanna.bookshelf.model.repository.UserRepository
import com.mbanna.bookshelf.model.roomdatabase.User
import com.mbanna.bookshelf.utils.Constants
import kotlinx.coroutines.*

class LoginViewModel(private val userRepository: UserRepository ) : ViewModel() {

    var userNotAvailable = MutableLiveData<Boolean>()
    var navigateToHomeScreen = MutableLiveData<Boolean>()
    var inValidPassword = MutableLiveData<Boolean>()
    var emptyUsername = MutableLiveData<Boolean>()
    var emptyPassword = MutableLiveData<Boolean>()
    var passwordVisibility = MutableLiveData<Boolean>(false)

    private suspend fun readLoginData(id: String, password: String): User? {
        return userRepository.readLoginData(id, password)
    }
    private suspend fun checkUserExist(id: String) : User?{
        return userRepository.getUserDetails(id)
    }

    fun perFormLogin(id: String, password: String){
        if(checkUserImplementation(id) == Constants.STATUS_INVALID){
            userNotAvailable.value = true
            return
        }

        when(isValidLoginData(id,password)){
            Constants.STATUS_OK ->
                navigateToHomeScreen.value = true
            Constants.STATUS_INVALID ->
                inValidPassword.value = true
        }
    }

    private fun checkUserImplementation(id: String) : Int{
        var result: Int = 1
        runBlocking {
            var job = viewModelScope.async(Dispatchers.IO) {
                val user = checkUserExist(id)
                if (user != null) {
                    Constants.STATUS_OK
                } else {
                    Constants.STATUS_INVALID
                }
            }
            result = job.await()
        }
        return result
    }

    fun isValidLoginData(id: String, password: String): Int {
        var result: Int = 1
        runBlocking {
            var job = viewModelScope.async(Dispatchers.IO) {
                val user = readLoginData(id, password)
                Log.i("Login Status ", "${user?.user_Name} and ${user?.user_Password}")
                if (user != null) {
                    Constants.STATUS_OK
                } else {
                    Constants.STATUS_INVALID
                }
            }
            result = job.await()
        }
        return result
    }

}
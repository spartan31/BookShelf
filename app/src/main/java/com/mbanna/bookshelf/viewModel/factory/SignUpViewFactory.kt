package com.mbanna.bookshelf.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mbanna.bookshelf.model.repository.UserRepository
import com.mbanna.bookshelf.viewModel.SignUpViewModel

class SignUpViewFactory(private val context: UserRepository) :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignUpViewModel(context) as T
    }
}
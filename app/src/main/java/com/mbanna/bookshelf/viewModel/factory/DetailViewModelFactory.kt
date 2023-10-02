package com.mbanna.bookshelf.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mbanna.bookshelf.viewModel.DetailViewModel

class DetailViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel() as T
    }
}
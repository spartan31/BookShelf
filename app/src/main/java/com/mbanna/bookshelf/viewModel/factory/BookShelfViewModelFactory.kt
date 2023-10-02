package com.mbanna.bookshelf.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mbanna.bookshelf.viewModel.BookShelfViewModel

class BookShelfViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BookShelfViewModel() as T
    }
}
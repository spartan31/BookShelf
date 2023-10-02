package com.mbanna.bookshelf.viewModel

import androidx.lifecycle.ViewModel
import com.mbanna.bookshelf.model.BookItem
import com.mbanna.bookshelf.model.repository.UserRepository

class BookShelfViewModel( ) : ViewModel() {
    var bookList : MutableList<BookItem> = ArrayList() ;

    var bookSortType : Boolean = false

    fun sortBasedOnSelection(){
        if (bookSortType){
           bookList.sortBy {it.title }
        }else{
            bookList.sortBy {it.hits }
        }
    }
}
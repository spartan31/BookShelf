package com.mbanna.bookshelf.viewModel

import androidx.lifecycle.ViewModel
import com.mbanna.bookshelf.model.BookItem
import com.mbanna.bookshelf.model.repository.UserRepository
import com.mbanna.bookshelf.utils.Constants
import com.mbanna.bookshelf.utils.FavoriteManager

class BookShelfViewModel( ) : ViewModel() {
    var bookList : MutableList<BookItem> = ArrayList() ;
    var favoriteIds : Set<String> = HashSet()

    var bookSortType : Int = 0

    fun sortBasedOnSelection(){
        when(bookSortType){
            Constants.SORT_BY_TITLE -> {
                bookList.sortBy {it.title }
            }

            Constants.SORT_BY_HITS -> {
                bookList.sortBy {it.hits }
            }

            Constants.SORT_BY_FAVOURITE -> {
                bookList.sortWith(compareByDescending<BookItem> { favoriteIds.contains(it.id) }
                .thenBy { it.title })
            }
        }
    }
}
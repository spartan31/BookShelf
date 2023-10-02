package com.mbanna.bookshelf.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mbanna.bookshelf.model.BookItem
import com.mbanna.bookshelf.utils.CommonUtils

class DetailViewModel  : ViewModel(){

    lateinit var bookItem: BookItem
    var title = MutableLiveData<String>()
    var lastUpdated = MutableLiveData<String>()
    var hints = MutableLiveData<String>()
    var alias = MutableLiveData<String>()

    fun performUpdate(){
        title.value = bookItem.title
        lastUpdated.value = "Last Updated : ${CommonUtils.convertUnixTimestampStringToFormattedDate(bookItem.lastChapterDate)}"
        hints.value = " Hits : ${bookItem.hits}"
        alias.value = "Alias : ${bookItem.alias}"
    }

}
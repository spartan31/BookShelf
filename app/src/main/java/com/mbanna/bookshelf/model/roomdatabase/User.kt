package com.mbanna.bookshelf.model.roomdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("user_table")
data class User(
    @PrimaryKey
    val user_Name: String,
    val user_Password: String,
    val user_Country: String
)
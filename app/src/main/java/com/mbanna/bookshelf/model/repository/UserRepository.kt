package com.mbanna.bookshelf.model.repository

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import com.mbanna.bookshelf.model.roomdatabase.AppDatabase
import com.mbanna.bookshelf.model.roomdatabase.User

class UserRepository( context: Context) {

    private val userDao = AppDatabase.getDatabase(context).getDao()
    suspend fun insertUser(user: User): Boolean {
        return try {
            userDao.insertUser(user)
            true
        } catch (e: SQLiteConstraintException) {
            false
        }
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun readLoginData(id: String, password: String): User? {
        return userDao.readLoginData(id, password)
    }

    suspend fun getUserDetails(id: String): User? {
        return userDao.getUserDetails(id)
    }

}
package com.mbanna.bookshelf.di

import android.content.Context
import com.mbanna.bookshelf.model.repository.UserRepository

class Provider private constructor(private val applicationContext: Context) {

    private var userRepository: UserRepository? = null

    fun getUserRepository(): UserRepository {
        return userRepository ?: synchronized(this) {
            userRepository ?: UserRepository(applicationContext).also {
                userRepository = it
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: Provider? = null

        fun getInstance(context: Context?): Provider {
            return INSTANCE ?: synchronized(this) {
                val instance = Provider(context!!.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}

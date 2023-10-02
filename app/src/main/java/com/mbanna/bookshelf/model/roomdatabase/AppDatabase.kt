package com.mbanna.bookshelf.model.roomdatabase

import android.content.Context
import androidx.room.*
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1 , exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getDao(): UserDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext, AppDatabase::class.java,"app_database"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}


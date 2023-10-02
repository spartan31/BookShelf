package com.mbanna.bookshelf.model.roomdatabase

import androidx.room.*

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM user_table WHERE user_name LIKE :id AND user_Password LIKE :password")
    suspend fun readLoginData(id :String , password : String) : User?

    @Query("select * from user_table where user_name LIKE :id")
    suspend fun getUserDetails(id: String ) : User?

}
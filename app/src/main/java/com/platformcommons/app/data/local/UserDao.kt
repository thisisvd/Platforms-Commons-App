package com.platformcommons.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User): Long

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM USER_TABLE LIMIT 1")
    fun getAllUsers(): Flow<List<User>>
}
package com.platformcommons.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.platformcommons.app.data.local.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User): Long

    @Query("DELETE FROM USER_TABLE WHERE userId=:userId")
    suspend fun deleteUser(userId: Int)

    @Query("SELECT * FROM USER_TABLE LIMIT 1")
    fun getAllUsers(): Flow<List<User>>
}
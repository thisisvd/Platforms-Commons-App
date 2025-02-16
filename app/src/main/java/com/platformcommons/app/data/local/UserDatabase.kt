package com.platformcommons.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.platformcommons.app.data.local.model.User

@Database(
    entities = [User::class], version = 1, exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {

    abstract val dao: UserDao
}
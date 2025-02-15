package com.platformcommons.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.platformcommons.app.utils.Constants.USER_TABLE

@Entity(tableName = USER_TABLE)
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val job: String,
    val name: String,
    val syncState: Boolean = false
)
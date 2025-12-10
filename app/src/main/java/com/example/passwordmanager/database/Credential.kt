package com.example.passwordmanager.database
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "credentials")
data class Credential(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val websiteName: String,
    val username: String,
    val password: String,
    val websiteUrl: String
)

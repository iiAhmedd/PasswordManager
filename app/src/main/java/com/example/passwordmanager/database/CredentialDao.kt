package com.example.passwordmanager.database
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CredentialDao {

    @Insert
    suspend fun insert(credential: Credential)

    @Delete
    suspend fun delete(credential: Credential)

    @androidx.room.Update
    suspend fun update(credential: Credential)

    @Query("SELECT * FROM credentials ORDER BY websiteName ASC")
    fun getAllCredentials(): Flow<List<Credential>>
}
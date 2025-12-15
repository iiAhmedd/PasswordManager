package com.example.passwordmanager

import com.example.passwordmanager.database.Credential
import com.example.passwordmanager.database.CredentialDao
import kotlinx.coroutines.flow.first

class CredentialRepository(private val dao: CredentialDao) {

    suspend fun insert(credential: Credential) {
        dao.insert(credential)
    }

    suspend fun update(credential: Credential) {
        dao.update(credential)
    }

    /**
     * Checks if a website exists.
     * Since we cannot modify the DAO to add a specific query,
     * we fetch the current list from the Flow and check it manually.
     */
    suspend fun websiteExists(websiteName: String): Boolean {
        // .first() grabs the current list of data from the database Flow
        val currentList = dao.getAllCredentials().first()
        return currentList.any { it.websiteName.equals(websiteName, ignoreCase = true) }
    }
}
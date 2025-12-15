package com.example.passwordmanager

import com.example.passwordmanager.database.Credential

class AddCredentialViewModel(private val repository: CredentialRepository) {

    suspend fun validateAndSave(
        id: Int,
        isEditMode: Boolean,
        website: String,
        username: String,
        password: String
    ): String {

        // Condition 1: Check if fields are empty
        if (website.isBlank() || username.isBlank() || password.isBlank()) {
            return "Fill all fields!"
        }

        // Condition 2: Check if website exists (Only if we are adding a NEW one)
        if (!isEditMode) {
            // We ask the repository (which we will mock in the test)
            if (repository.websiteExists(website.trim())) {
                return "Website already exists!"
            }
        }

        // If logic passes, save data
        val autoUrl = "https://www.${website.trim().lowercase()}.com"
        val credential = Credential(
            id = if (isEditMode) id else 0,
            websiteName = website.trim(),
            username = username.trim(),
            password = password,
            websiteUrl = autoUrl
        )

        if (isEditMode) {
            repository.update(credential)
        } else {
            repository.insert(credential)
        }

        return "Success"
    }
}
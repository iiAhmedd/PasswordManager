package com.example.passwordmanager

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import com.example.passwordmanager.database.AppDatabase
import kotlinx.coroutines.launch

class AddCredentialActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. Setup the Database, Repository, and ViewModel
        val db = AppDatabase.getDatabase(this)
        val repository = CredentialRepository(db.credentialDao())
        val viewModel = AddCredentialViewModel(repository)

        val credentialId = intent.getIntExtra("id", -1)
        val isEditMode = credentialId != -1
        val existingWebsite = intent.getStringExtra("website") ?: ""
        val existingUsername = intent.getStringExtra("username") ?: ""
        val existingPassword = intent.getStringExtra("password") ?: ""

        setContent {
            val context = LocalContext.current

            // 2. Pass the Logic to the UI
            AddCredentialScreen(
                isEditMode = isEditMode,
                initialWebsite = existingWebsite,
                initialUsername = existingUsername,
                initialPassword = existingPassword,
                onSave = { website, username, password ->
                    
                    lifecycleScope.launch {
                        // 3. Call the ViewModel validation
                        val result = viewModel.validateAndSave(
                            id = credentialId,
                            isEditMode = isEditMode,
                            website = website,
                            username = username,
                            password = password
                        )

                        if (result == "Success") {
                            finish()
                        } else {
                            Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun AddCredentialScreen(
    isEditMode: Boolean,
    initialWebsite: String,
    initialUsername: String,
    initialPassword: String,
    onSave: (String, String, String) -> Unit
) {
    var website by remember { mutableStateOf(initialWebsite) }
    var username by remember { mutableStateOf(initialUsername) }
    var password by remember { mutableStateOf(initialPassword) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = if (isEditMode) "Edit Password" else "Add New Password",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = website, onValueChange = { website = it },
            label = { Text("Website Name") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = username, onValueChange = { username = it },
            label = { Text("Username") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Password") }, modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onSave(website, username, password) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isEditMode) "Update Password" else "Save Password")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddCredentialScreenPreview() {
    MaterialTheme {
        AddCredentialScreen(
            isEditMode = false,
            initialWebsite = "",
            initialUsername = "",
            initialPassword = "",
            onSave = { _, _, _ -> }
        )
    }
}
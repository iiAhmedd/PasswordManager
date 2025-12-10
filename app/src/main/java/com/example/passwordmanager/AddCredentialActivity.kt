package com.example.passwordmanager

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.passwordmanager.database.AppDatabase
import com.example.passwordmanager.database.Credential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddCredentialActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.getDatabase(this)


        val credentialId = intent.getIntExtra("id", -1)
        val isEditMode = credentialId != -1


        val existingWebsite = intent.getStringExtra("website") ?: ""
        val existingUsername = intent.getStringExtra("username") ?: ""
        val existingPassword = intent.getStringExtra("password") ?: ""

        setContent {
            var website by remember { mutableStateOf(existingWebsite) }
            var username by remember { mutableStateOf(existingUsername) }
            var password by remember { mutableStateOf(existingPassword) }

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
                    onClick = {
                        if (website.isNotEmpty() && password.isNotEmpty()) {
                            val autoUrl = "https://www.${website.trim().lowercase()}.com"
                            val credential = Credential(
                                id = if (isEditMode) credentialId else 0, // 0 means new ID
                                websiteName = website,
                                username = username,
                                password = password,
                                websiteUrl = autoUrl
                            )

                            lifecycleScope.launch(Dispatchers.IO) {
                                if (isEditMode) {
                                    db.credentialDao().update(credential)
                                } else {
                                    db.credentialDao().insert(credential)
                                }
                                finish()
                            }
                        } else {
                            Toast.makeText(this@AddCredentialActivity, "Fill all fields!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (isEditMode) "Update Password" else "Save Password")
                }
            }
        }
    }
}
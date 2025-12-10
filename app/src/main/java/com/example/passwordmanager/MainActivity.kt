package com.example.passwordmanager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import com.example.passwordmanager.database.AppDatabase
import com.example.passwordmanager.ui.theme.PasswordManagerTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val db = AppDatabase.getDatabase(this)

        setContent {

            PasswordManagerTheme {
                val credentials by db.credentialDao().getAllCredentials().collectAsState(initial = emptyList())
                val context = LocalContext.current

                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            val intent = Intent(context, AddCredentialActivity::class.java)
                            startActivity(intent)
                        }) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                        }
                    }
                ) { innerPadding ->
                    LazyColumn(contentPadding = innerPadding) {
                        items(credentials) { item ->
                            CredentialItem(
                                credential = item,
                                onDelete = {
                                    lifecycleScope.launch(Dispatchers.IO) {
                                        db.credentialDao().delete(item)
                                    }
                                },
                                onEdit = { cred ->
                                    val intent = Intent(context, AddCredentialActivity::class.java).apply {
                                        putExtra("id", cred.id)
                                        putExtra("website", cred.websiteName)
                                        putExtra("username", cred.username)
                                        putExtra("password", cred.password)
                                    }
                                    startActivity(intent)
                                },
                                onOpenLink = { url ->
                                    if (url.isNotEmpty()) {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                        startActivity(intent)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
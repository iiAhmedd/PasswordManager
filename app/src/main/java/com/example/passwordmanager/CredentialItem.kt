package com.example.passwordmanager

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.passwordmanager.database.Credential

@Composable
fun CredentialItem(
    credential: Credential,
    onDelete: () -> Unit,
    onEdit: (Credential) -> Unit,
    onOpenLink: (String) -> Unit
) {

    var isVisible by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = credential.websiteName,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )


            Text(
                text = "User: ${credential.username}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))


            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Password: ",
                    style = MaterialTheme.typography.bodyMedium
                )


                Text(
                    text = if (isVisible) credential.password else "••••••••",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge
                )


                IconButton(onClick = { isVisible = !isVisible }) {
                    Icon(
                        imageVector = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle Password"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))


            Row {
                Button(onClick = { onOpenLink(credential.websiteUrl) }) {
                    Text("Visit")
                }
                Spacer(modifier = Modifier.width(8.dp))


                Button(onClick = { onEdit(credential) }) {
                    Text("Edit")
                }

                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CredentialItemPreview() {
    //dummy data
    val sampleCredential = Credential(
        id = 1,
        websiteName = "Facebook",
        username = "user@gmail.com",
        password = "Password123",
        websiteUrl = "https://facebook.com"
    )

    MaterialTheme {
        CredentialItem(
            credential = sampleCredential,
            onDelete = {},
            onEdit = {},
            onOpenLink = {}
        )
    }
}
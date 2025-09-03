
package com.example.firstapp.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firstapp.feature.home.model.User
import com.example.firstapp.network.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersListPage() {
    var users by remember { mutableStateOf<List<User>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Load data on launch
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                isLoading = true
                errorMessage = null

                val response = RetrofitClient.api.getAllUsers()

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        if (body.status) {
                            users = body.users
                            errorMessage = null
                        } else {
                            errorMessage = body.message.ifEmpty { "Unknown server error" }
                        }
                    } else {
                        errorMessage = "Empty response from server"
                    }
                } else {
                    errorMessage = "Server error: HTTP ${response.code()}"
                }
            } catch (e: IOException) {
                errorMessage = "Network error: Please check your connection"
            } catch (e: HttpException) {
                errorMessage = "HTTP error: ${e.code()}"
            } catch (e: Exception) {
                errorMessage = "Unexpected error: ${e.localizedMessage ?: "Unknown error"}"
            } finally {
                isLoading = false
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        TopAppBar(
            title = {
                Text(
                    text = "Users List",
                    fontSize = 20.sp
                )
            }
        )

        // Content
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Loading users...")
                        }
                    }
                }

                errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "⚠ Error",
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = errorMessage!!,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    scope.launch {
                                        isLoading = true
                                        errorMessage = null
                                        try {
                                            val response = RetrofitClient.api.getAllUsers()
                                            if (response.isSuccessful) {
                                                val body = response.body()
                                                if (body != null && body.status) {
                                                    users = body.users
                                                } else {
                                                    errorMessage = body?.message ?: "Invalid response"
                                                }
                                            } else {
                                                errorMessage = "Server error: ${response.code()}"
                                            }
                                        } catch (e: Exception) {
                                            errorMessage = "Error: ${e.localizedMessage}"
                                        } finally {
                                            isLoading = false
                                        }
                                    }
                                }
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }

                users.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No users found")
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = users,
                            key = { user -> user.id }
                        ) { user ->
                            UserItem(user = user)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "ID: ${user.id}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = user.email,
                fontSize = 16.sp,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Created: ${user.createdAt}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
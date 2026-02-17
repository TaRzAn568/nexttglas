package com.nexttglas.nexttglas.ui.screen.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.facebook.CallbackManager
import com.nexttglas.nexttglas.ui.components.DividerWithText
import com.nexttglas.nexttglas.ui.components.FormTextField
import com.nexttglas.nexttglas.viewmodel.AuthState
import com.nexttglas.nexttglas.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    fbCallbackManager: CallbackManager? = null,
    onLoginSuccess: () -> Unit
) {
    val authState by viewModel.authState.collectAsState()

    val (googleSignInClient, googleLauncher) = rememberGoogleSignInLauncher(
        viewModel = viewModel
    )

    val facebookLoginAction = fbCallbackManager?.let {
        rememberFacebookSignInLauncher(
            viewModel = viewModel,
            fbCallbackManager = it
        )
    }

    // Form state
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Validation state
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    // React to state changes
    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onLoginSuccess()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Nexttglas",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 0.5.sp
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { /* Handle menu */ }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Social Login Section
                Button(
                    onClick = {
                        googleLauncher.launch(googleSignInClient.signInIntent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = authState !is AuthState.Loading
                ) {
                    Text(if (authState is AuthState.Loading) "Signing in..." else "Continue with Google")
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (facebookLoginAction != null) {
                    Button(
                        onClick = {
                            facebookLoginAction()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = authState !is AuthState.Loading
                    ) {
                        Text(if (authState is AuthState.Loading) "Signing in..." else "Continue with Facebook")
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Divider with "OR"
                DividerWithText(text = "OR")

                Spacer(modifier = Modifier.height(24.dp))

                // Login Form Section
                Text(
                    text = "Login to Your Account",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Email Field
                FormTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = false
                    },
                    label = "Email",
                    placeholder = "Enter your email",
                    leadingIcon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email,
                    isError = emailError,
                    errorMessage = "Valid email is required"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Password Field
                FormTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = false
                    },
                    label = "Password",
                    placeholder = "Enter your password",
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true,
                    isError = passwordError,
                    errorMessage = "Password is required"
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Login Button
                Button(
                    onClick = {
                        // Validate fields
                        emailError = email.isBlank() || !email.contains("@")
                        passwordError = password.isBlank()

                        // If all fields are valid, login
                        if (!emailError && !passwordError) {
                            viewModel.login(email, password)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = authState !is AuthState.Loading
                ) {
                    Text(
                        text = if (authState is AuthState.Loading) "Logging in..." else "Login",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Show error if exists
                if (authState is AuthState.Error) {
                    Text(
                        text = (authState as AuthState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

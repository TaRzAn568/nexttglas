package com.nexttglas.nexttglas.ui.screen.auth

import android.net.Uri
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
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
import com.nexttglas.nexttglas.R
import com.nexttglas.nexttglas.data.SignUpData
import com.nexttglas.nexttglas.ui.components.DividerWithText
import com.nexttglas.nexttglas.ui.components.FormDropdown
import com.nexttglas.nexttglas.ui.components.FormTextField
import com.nexttglas.nexttglas.ui.components.PhotoPicker
import com.nexttglas.nexttglas.viewmodel.AuthState
import com.nexttglas.nexttglas.viewmodel.AuthViewModel
import com.nexttglas.nexttglas.viewmodel.LoginMethod

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: AuthViewModel,
    fbCallbackManager: CallbackManager,
    onLoginClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
    onLoginSuccess: () -> Unit,
    onLoginWithFaceBookClicked: () -> Unit,
    onBackClick: () -> Unit = {}
) {
    val authState by viewModel.authState.collectAsState()

    val (googleSignInClient, googleLauncher) = rememberGoogleSignInLauncher(
        viewModel = viewModel
    )

    val facebookLoginAction = rememberFacebookSignInLauncher(
        viewModel = viewModel, fbCallbackManager
    )

    // Form state
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    // Validation state
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var fullNameError by remember { mutableStateOf(false) }
    var countryError by remember { mutableStateOf(false) }
    var genderError by remember { mutableStateOf(false) }

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
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
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
                    val isGoogleLoading = authState is AuthState.Loading && (authState as AuthState.Loading).method == LoginMethod.GOOGLE
                    Text(if (isGoogleLoading) "Signing in..." else "Continue with Google")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        facebookLoginAction()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = authState !is AuthState.Loading
                ) {
                    val isFacebookLoading = authState is AuthState.Loading && (authState as AuthState.Loading).method == LoginMethod.FACEBOOK
                    Text(if (isFacebookLoading) "Signing in..." else "Continue with Facebook")
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Divider with "OR"
                DividerWithText(text = "OR")

                Spacer(modifier = Modifier.height(24.dp))

                // Signup Form Section
                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Photo Picker
                PhotoPicker(
                    photoUri = photoUri,
                    onPhotoSelected = { photoUri = it }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Full Name Field
                FormTextField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                        fullNameError = false
                    },
                    label = "Full Name",
                    placeholder = "Enter your full name",
                    leadingIcon = Icons.Default.Person,
                    isError = fullNameError,
                    errorMessage = "Full name is required"
                )

                Spacer(modifier = Modifier.height(12.dp))

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
                    errorMessage = "Password must be at least 6 characters"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Country Dropdown
                FormDropdown(
                    value = country,
                    onValueChange = {
                        country = it
                        countryError = false
                    },
                    label = "Country",
                    options = listOf(
                        "United States", "Canada", "United Kingdom", "Australia",
                        "Germany", "France", "India", "China", "Japan", "Other"
                    ),
                    placeholder = "Select your country",
                    leadingIcon = Icons.Default.Place,
                    isError = countryError,
                    errorMessage = "Country is required"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Gender Dropdown
                FormDropdown(
                    value = gender,
                    onValueChange = {
                        gender = it
                        genderError = false
                    },
                    label = "Gender",
                    options = listOf("Male", "Female", "Other", "Prefer not to say"),
                    placeholder = "Select your gender",
                    leadingIcon = Icons.Default.Person,
                    isError = genderError,
                    errorMessage = "Gender is required"
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Submit Button
                Button(
                    onClick = {
                        // Validate fields
                        emailError = email.isBlank() || !email.contains("@")
                        passwordError = password.length < 6
                        fullNameError = fullName.isBlank()
                        countryError = country.isBlank()
                        genderError = gender.isBlank()

                        // If all fields are valid, submit
                        if (!emailError && !passwordError && !fullNameError && !countryError && !genderError) {
                            val signUpData = SignUpData(
                                email = email,
                                password = password,
                                fullName = fullName,
                                country = country,
                                gender = gender,
                                photoUri = photoUri
                            )
                            viewModel.signUpWithDetails(signUpData)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = authState !is AuthState.Loading
                ) {
                    val isEmailLoading = authState is AuthState.Loading && (authState as AuthState.Loading).method == LoginMethod.EMAIL
                    Text(
                        text = if (isEmailLoading) "Creating Account..." else "Sign Up",
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

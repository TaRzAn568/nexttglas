package com.nexttglas.nexttglas.ui.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nexttglas.nexttglas.viewmodel.AuthState
import com.nexttglas.nexttglas.viewmodel.AuthViewModel
import com.facebook.CallbackManager
import com.nexttglas.nexttglas.R

@Composable
fun MainScreen(
    viewModel: AuthViewModel,
    fbCallbackManager: CallbackManager,
    onLoginClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
    onLoginSuccess: () -> Unit,
    onLoginWithFaceBookClicked: () -> Unit
) {
    val authState by viewModel.authState.collectAsState()

    val (googleSignInClient, googleLauncher) = rememberGoogleSignInLauncher(
        viewModel = viewModel
    )

    val facebookLoginAction = rememberFacebookSignInLauncher(
        viewModel = viewModel, fbCallbackManager
    )

    // React to state changes
    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onLoginSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.nextglas_logo),
            contentDescription = "Confess It Logo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp, 60.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,


        ) {
        Button(
            onClick = {
                googleLauncher.launch(googleSignInClient.signInIntent)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = authState !is AuthState.Loading
        ) {
            Text(if (authState is AuthState.Loading) "Signing in..." else "Login with Google")
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = {
                facebookLoginAction()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = authState !is AuthState.Loading
        ) {
            Text(if (authState is AuthState.Loading) "Signing in..." else "Login with Facebook")
        }
        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = onLoginClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = onSignUpClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("SignUp")
        }

    }
}

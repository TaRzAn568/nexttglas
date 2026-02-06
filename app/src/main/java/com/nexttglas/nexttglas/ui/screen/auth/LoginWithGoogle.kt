package com.nexttglas.nexttglas.ui.screen.auth

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.nexttglas.nexttglas.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.nexttglas.nexttglas.R

@Composable
fun rememberGoogleSignInLauncher(
    viewModel: AuthViewModel,
    onError: (String) -> Unit = {}
): Pair<GoogleSignInClient, ManagedActivityResultLauncher<Intent, ActivityResult>> {
    val context = LocalContext.current

    val googleSignInClient = remember {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, options)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let {
                // Call ViewModel function to handle Google Sign-In
                viewModel.loginWithGoogle(it)
            } ?: run {
                onError("Failed to get ID token")
            }
        } catch (e: ApiException) {
            onError("Google Sign-In failed: ${e.message}")
        } catch (e: Exception) {
            onError("An error occurred: ${e.message}")
        }
    }

    return Pair(googleSignInClient, launcher)
}
package com.nexttglas.nexttglas.ui.screen.auth

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.nexttglas.nexttglas.viewmodel.AuthViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

@Composable
fun rememberFacebookSignInLauncher(
    viewModel: AuthViewModel,
    fbCallbackManager: CallbackManager
): () -> Unit {

    val context = LocalContext.current
    val activity = context as Activity



    // Register callback once
    LaunchedEffect(Unit) {
        LoginManager.getInstance().registerCallback(
            fbCallbackManager,
            object : FacebookCallback<LoginResult> {

                override fun onSuccess(result: LoginResult) {
                    val token = result.accessToken.token
                    viewModel.loginWithFacebook(token)
                }

                override fun onCancel() {}

                override fun onError(error: FacebookException) {
                    Log.e("FB_LOGIN", error.message ?: "FB Error")
                }
            }
        )
    }

    return {
        LoginManager.getInstance()
            .logInWithReadPermissions(
                activity,
                listOf("email", "public_profile")
            )
    }
}
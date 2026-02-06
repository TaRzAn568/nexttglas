package com.nexttglas.nexttglas.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

import dagger.hilt.android.EntryPointAccessors
import com.facebook.CallbackManager
import com.nexttglas.nexttglas.data.AuthRepository
import com.nexttglas.nexttglas.navigation.AppNavHost
import com.nexttglas.nexttglas.ui.theme.NexttglasTheme
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppEntryPoint {
    fun authRepository(): AuthRepository
}

@Composable
fun App(fbCallbackManager: CallbackManager) {
    val context = LocalContext.current
    val appEntryPoint = EntryPointAccessors.fromApplication(
        context.applicationContext,
        AppEntryPoint::class.java
    )
    val authRepository = appEntryPoint.authRepository()

    NexttglasTheme {
        AppNavHost(authRepository = authRepository, fbCallbackManager)
    }
}
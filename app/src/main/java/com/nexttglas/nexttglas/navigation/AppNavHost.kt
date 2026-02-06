package com.nexttglas.nexttglas.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nexttglas.nexttglas.ui.screen.SplashScreen
import com.nexttglas.nexttglas.ui.screen.auth.LoginScreen
import com.nexttglas.nexttglas.ui.screen.HomeScreen
import com.nexttglas.nexttglas.ui.screen.auth.MainScreen
import com.nexttglas.nexttglas.ui.screen.auth.RegistrationScreen
import com.nexttglas.nexttglas.viewmodel.AuthViewModel
import com.facebook.CallbackManager
import com.nexttglas.nexttglas.data.AuthRepository

@Composable
fun AppNavHost(
    authRepository: AuthRepository,
    fbCallbackManager: CallbackManager
) {
    val navController = rememberNavController()
    val viewModel: AuthViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onAnimationFinished = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Main.route) {
            MainScreen(
                viewModel = viewModel,
                fbCallbackManager,
                onLoginClicked = { navController.navigate(Screen.Login.route) },
                onSignUpClicked = { navController.navigate(Screen.Signup.route) },
                onLoginSuccess = { navController.navigate(Screen.Home.route) },
                onLoginWithFaceBookClicked = { navController.navigate(Screen.LoginWithFaceBook.route) }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = { navController.navigate(Screen.Home.route) })
        }
        composable(Screen.Signup.route) {
            RegistrationScreen(
                viewModel = viewModel,
                onSignupSuccess = { navController.navigate(Screen.Home.route) })
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onLogout = {
                    authRepository.logout()
                    viewModel.resetState()
                    navController.navigate(Screen.Main.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }


}

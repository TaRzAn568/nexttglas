package com.nexttglas.nexttglas.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nexttglas.nexttglas.ui.screen.SplashScreen
import com.nexttglas.nexttglas.ui.screen.auth.LoginScreen
import com.nexttglas.nexttglas.ui.screen.auth.MainScreen
import com.nexttglas.nexttglas.ui.screen.auth.RegistrationScreen
import com.nexttglas.nexttglas.viewmodel.AuthViewModel
import com.facebook.CallbackManager
import com.nexttglas.nexttglas.data.AuthRepository
import com.nexttglas.nexttglas.ui.screen.HomeScreen
import com.nexttglas.nexttglas.ui.screen.IntroScreen
import com.nexttglas.nexttglas.ui.screen.RegistrationPreferenceScreen

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
                    if (authRepository.isUserLoggedIn()) {
                        navController.navigate(Screen.RegistrationPreferenceScreen.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.IntroScreen.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Screen.IntroScreen.route) {
            IntroScreen (
                onSignUpClick = { navController.navigate(Screen.Main.route) },
                onLoginClick =  { navController.navigate(Screen.Login.route)}
            )
        }

        composable(Screen.Main.route) {
            MainScreen(
                viewModel = viewModel,
                fbCallbackManager,
                onLoginClicked = { navController.navigate(Screen.Login.route) },
                onSignUpClicked = { navController.navigate(Screen.Signup.route) },
                onLoginSuccess = {
                    navController.navigate(Screen.RegistrationPreferenceScreen.route) {
                        popUpTo(Screen.IntroScreen.route) { inclusive = true }
                    }
                },
                onLoginWithFaceBookClicked = { navController.navigate(Screen.LoginWithFaceBook.route) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = viewModel,
                fbCallbackManager = fbCallbackManager,
                onLoginSuccess = {
                    navController.navigate(Screen.RegistrationPreferenceScreen.route) {
                        popUpTo(Screen.IntroScreen.route) { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Screen.Signup.route) {
            RegistrationScreen(
                viewModel = viewModel,
                onSignupSuccess = { navController.navigate(Screen.RegistrationPreferenceScreen.route) })
        }

        composable(Screen.RegistrationPreferenceScreen.route) {
            RegistrationPreferenceScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onLogout = {
                    viewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen {}
        }
    }


}

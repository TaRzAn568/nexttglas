package com.nexttglas.nexttglas.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("Splash")

    object IntroScreen : Screen("IntroScreen")
    object Main : Screen("Main")
    object Login : Screen("Login")
    object Signup : Screen("Signup")
    object Home : Screen("Home")
    object LoginWithGoogle : Screen("GoogleLogin")
    object LoginWithFaceBook : Screen("FacebookLogin")
}
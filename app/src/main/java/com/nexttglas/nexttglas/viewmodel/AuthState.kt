package com.nexttglas.nexttglas.viewmodel

sealed class AuthState {
    object Idle : AuthState()
    data class Loading(val method: LoginMethod = LoginMethod.EMAIL) : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

enum class LoginMethod {
    EMAIL,
    GOOGLE,
    FACEBOOK
}

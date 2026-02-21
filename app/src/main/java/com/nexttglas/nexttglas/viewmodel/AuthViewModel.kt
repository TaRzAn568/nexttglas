package com.nexttglas.nexttglas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexttglas.nexttglas.data.AuthRepository
import com.nexttglas.nexttglas.data.SignUpData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading(LoginMethod.EMAIL)
            val result = repo.login(email, password)

            _authState.value = result.fold(
                onSuccess = { AuthState.Success },
                onFailure = { AuthState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun signup(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading(LoginMethod.EMAIL)
            val result = repo.signUp(email, password)

            _authState.value = result.fold(
                onSuccess = { AuthState.Success },
                onFailure = { AuthState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun signUpWithDetails(signUpData: SignUpData) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading(LoginMethod.EMAIL)
            val result = repo.signUpWithDetails(signUpData)

            _authState.value = result.fold(
                onSuccess = { AuthState.Success },
                onFailure = { AuthState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading(LoginMethod.GOOGLE)
            val result = repo.loginWithGoogle(idToken)

            _authState.value = result.fold(
                onSuccess = { AuthState.Success },
                onFailure = { AuthState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun loginWithFacebook(accessToken: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading(LoginMethod.FACEBOOK)

            val result = repo.loginWithFacebook(accessToken)

            _authState.value = result.fold(
                onSuccess = { AuthState.Success },
                onFailure = { AuthState.Error(it.message ?: "FB login failed") }
            )
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }

    fun logout() {
        repo.logout()
        _authState.value = AuthState.Idle
    }

    fun isUserLoggedIn(): Boolean {
        return repo.isUserLoggedIn()
    }
}

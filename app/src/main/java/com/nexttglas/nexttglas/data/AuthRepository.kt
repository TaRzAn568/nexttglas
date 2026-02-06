package com.nexttglas.nexttglas.data

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun signUp(email: String, password: String): Result<Unit>
    suspend fun loginWithGoogle(idToken: String): Result<Unit>
    suspend fun loginWithFacebook(accessToken: String): Result<Unit>
    fun logout()
    fun isUserLoggedIn(): Boolean
}
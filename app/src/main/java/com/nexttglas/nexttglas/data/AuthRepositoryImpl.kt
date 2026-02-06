package com.nexttglas.nexttglas.data

import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    fun currentUser() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            val result = firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()
            Result.success(Unit)

        } catch (e: Exception) {
            return Result.failure(e)
        }
    }


    override suspend fun signUp(
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            val result = firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun loginWithGoogle(idToken: String): Result<Unit> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential).await()
            Result.success(Unit)
        }
        catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun loginWithFacebook(accessToken: String): Result<Unit> {
        return try {
            val credential = FacebookAuthProvider.getCredential(accessToken)
            FirebaseAuth.getInstance()
                .signInWithCredential(credential)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }


}
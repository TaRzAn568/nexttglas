package com.nexttglas.nexttglas.data

import android.content.Context
import android.net.Uri
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nexttglas.nexttglas.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
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

    override suspend fun signUpWithDetails(signUpData: SignUpData): Result<Unit> {
        return try {
            // Create user with email and password
            val authResult = firebaseAuth
                .createUserWithEmailAndPassword(signUpData.email, signUpData.password)
                .await()

            val user = authResult.user ?: return Result.failure(Exception("User creation failed"))

            // Upload photo if provided
            var photoUrl: String? = null
            signUpData.photoUri?.let { uri ->
                photoUrl = uploadProfilePhoto(user.uid, uri)
            }

            // Update user profile with display name and photo
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(signUpData.fullName)
                .apply {
                    photoUrl?.let { setPhotoUri(Uri.parse(it)) }
                }
                .build()

            user.updateProfile(profileUpdates).await()

            // Save additional user data to Firestore
            val userData = hashMapOf(
                "uid" to user.uid,
                "email" to signUpData.email,
                "fullName" to signUpData.fullName,
                "country" to signUpData.country,
                "gender" to signUpData.gender,
                "photoUrl" to (photoUrl ?: ""),
                "createdAt" to System.currentTimeMillis()
            )

            firestore.collection("users")
                .document(user.uid)
                .set(userData)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun uploadProfilePhoto(userId: String, photoUri: Uri): String? {
        return try {
            val storageRef = storage.reference
                .child("profile_photos/$userId/${System.currentTimeMillis()}.jpg")

            storageRef.putFile(photoUri).await()
            storageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            null
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
        // Sign out from Firebase
        firebaseAuth.signOut()

        // Sign out from Google Sign-In
        try {
            val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)
            googleSignInClient.signOut()
        } catch (e: Exception) {
            // Ignore error if Google Sign-In client fails to sign out
        }

        // Sign out from Facebook
        try {
            LoginManager.getInstance().logOut()
        } catch (e: Exception) {
            // Ignore error if Facebook fails to sign out
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }


}
package com.nexttglas.nexttglas.data

import android.net.Uri

data class SignUpData(
    val email: String,
    val password: String,
    val fullName: String,
    val country: String,
    val gender: String,
    val photoUri: Uri? = null
)

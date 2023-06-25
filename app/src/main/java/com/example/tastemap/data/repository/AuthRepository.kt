package com.example.tastemap.data.repository

import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber
import javax.inject.Inject
import kotlin.Exception

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) {
    fun createAccount(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("currentUserWithEmail: Success")
                    onSuccess()
                } else {
                    Timber.e("createUserWithEmail: Failure", task.exception)
                    val exception = task.exception ?: Exception("Unknown Error")
                    onFailure(exception)
                }
            }
    }

    fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("signInWithEmail: Success")
                    onSuccess()
                } else {
                    Timber.e("signInWithEmail: Failure", task.exception)
                    val exception = task.exception ?: Exception("Unknown Error")
                    onFailure(exception)
                }
            }
    }

    fun signOut() {
        auth.signOut()
    }
}
package com.example.tastemap.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend fun createAccount(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        withContext(defaultDispatcher) {
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

    }

    suspend fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        withContext(defaultDispatcher) {
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

    }

    suspend fun signOut() {
        withContext(defaultDispatcher) {
            auth.signOut()
        }
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    suspend fun fetchCurrentUser(): FirebaseUser? {
        return withContext(defaultDispatcher) {
            auth.currentUser
        }
    }
}

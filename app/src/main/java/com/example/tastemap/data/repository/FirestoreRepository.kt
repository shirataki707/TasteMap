package com.example.tastemap.data.repository

import com.example.tastemap.data.model.UserPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject


// [NOTE] callbackだらけじゃなくて，ちゃんと値を返そう. addUserDetailsはsuspendにしたい
class FirestoreRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    private val documentRef: DocumentReference by lazy {
        firestore.collection("users").document(auth.currentUser!!.uid)
    }

    fun addUserDetails(
        userName: String,
        userPreferences: UserPreferences,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        documentRef
            .set(
                mapOf(
                    "username" to userName,
                    "preferences" to userPreferences
                )
            )
            .addOnSuccessListener {
                Timber.d("user details added")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Timber.w("Error adding user details", e)
                onFailure(e)
            }

    }

    suspend fun fetchUserDetails(
        onSuccess: (String, UserPreferences) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        withContext(defaultDispatcher) {
            documentRef
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val userName = document.get("username") as String
                        val userPreferencesMap = document.get("preferences")

                        if (userName != null && userPreferencesMap is Map<*, *>) {
                            // [NOTE] firestoreではデフォルトの数値はLongっぽいから後でIntにキャスト
                            val userPreferences = UserPreferences(
                                reviewPriorities = (userPreferencesMap["reviewPriorities"] as? Long ?: 0L).toInt(),
                                smokingPriorities = (userPreferencesMap["smokingPriorities"] as? Long ?: 0L).toInt()
                            )
                            onSuccess(userName, userPreferences)
                        } else {
                            onFailure(Exception("Incomplete user details."))
                        }
                    } else {
                        onFailure(Exception("User not found."))
                    }
                }
                .addOnFailureListener { e ->
                    Timber.w("Error getting user details", e)
                    onFailure(e)
                }
        }
    }
}

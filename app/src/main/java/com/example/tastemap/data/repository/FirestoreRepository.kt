package com.example.tastemap.data.repository

import com.example.tastemap.data.model.UserPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class FirestoreRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    private val documentRef: DocumentReference by lazy {
        firestore.collection("users").document(auth.currentUser!!.uid)
    }


//    private fun addPreference(
//        userPreferences: UserPreferences,
//        onSuccess: () -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        documentRef.collection("preferences")
//            .add(userPreferences)
//            .addOnSuccessListener { documentReference ->
//                Timber.d("DocumentSnapshot added with ID: ${documentReference.id}")
//                onSuccess()
//            }
//            .addOnFailureListener { e ->
//                Timber.w("Error adding document", e)
//                onFailure(e)
//            }
//    }

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

//    fun getUserDetails() {
//        documentRef.get()
//            .addOnSuccessListener { result ->
//                Timber.d("get result: $result")
//            }
//            .addOnFailureListener { exception ->
//                Timber.w("Error getting documents", exception)
//            }
//
//    }
    fun getUserDetails(
        onSuccess: (String, UserPreferences) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        documentRef
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val userName = document.get("username") as String
                    val userPreferencesMap = document.get("preferences")

                    if (userName != null && userPreferencesMap is Map<*, *>) {
                        val userPreferences = UserPreferences(
                            genre = userPreferencesMap["genre"] as? String ?: "",
                            isSmoker = userPreferencesMap["isSmoker"] as? Boolean ?: false
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
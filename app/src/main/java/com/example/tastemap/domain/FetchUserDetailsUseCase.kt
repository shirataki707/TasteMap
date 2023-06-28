package com.example.tastemap.domain

import com.example.tastemap.data.model.UserPreferences
import com.example.tastemap.data.repository.FirestoreRepository
import timber.log.Timber
import javax.inject.Inject

class FetchUserDetailsUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) {
    suspend operator fun invoke(
        onSuccess: (String, UserPreferences) -> Unit,
        onFailure: (String) -> Unit)
    {
        try {
            firestoreRepository.fetchUserDetails(
                onSuccess = { name, preferences ->
                    Timber.d("user: $name, preferences: $preferences")
                    onSuccess(name, preferences)
                },
                onFailure = { e -> onFailure(e.message.toString()) }
            )
        } catch (e: Exception) {
            onFailure(e.message.toString())
        }
    }
}
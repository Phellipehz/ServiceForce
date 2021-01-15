package com.serviceforce.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.serviceforce.models.User
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun createUpdate(user: User): User? {
        return suspendCoroutine { continuation ->
            firestore.collection("users").document(user.uid).set(user, SetOptions.merge())
                .addOnSuccessListener { continuation.resume(user) }
                .addOnFailureListener { exception -> continuation.resumeWithException(exception) }
        }
    }

    suspend fun find(uid: String): User? {
        return suspendCoroutine { continuation ->
            firestore.collection("users").document(uid).get()
                .addOnSuccessListener { querySnapshot -> continuation.resume(querySnapshot.toObject<User>()) }
                .addOnFailureListener { exception -> continuation.resumeWithException(exception) }
        }
    }


}
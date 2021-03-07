package com.serviceforce.repository.firebase

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthenticationRepository {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun authenticate(email: String, password: String): AuthResult {
        return suspendCoroutine {  continuation ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult -> continuation.resume(authResult) }
                .addOnFailureListener { exception -> continuation.resumeWithException(exception) }
        }
    }

}
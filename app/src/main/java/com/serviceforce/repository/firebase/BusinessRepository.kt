package com.serviceforce.repository.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.serviceforce.models.Business
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class BusinessRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun find(businessCode: String): Business? {
        return suspendCoroutine { continuation ->
            firestore.collection("businesses").document(businessCode).get()
                .addOnSuccessListener { snapshot -> continuation.resume(snapshot.toObject<Business>()) }
                .addOnFailureListener { exception -> continuation.resumeWithException(exception) }
        }
    }

}
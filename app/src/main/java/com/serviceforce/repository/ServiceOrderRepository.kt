package com.serviceforce.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.serviceforce.models.ServiceOrder
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ServiceOrderRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun createUpdate(serviceOrder: ServiceOrder, businessCode: String): ServiceOrder? {
        return suspendCoroutine { continuation ->
            firestore.collection("businesses").document(businessCode).collection("orders").add(serviceOrder)
                .addOnSuccessListener { documentReference ->
                    documentReference.get()
                        .addOnSuccessListener { documentSnapshot -> continuation.resume(documentSnapshot.toObject<ServiceOrder>()) }
                        .addOnFailureListener { exception -> continuation.resumeWithException(exception) }
                }
                .addOnFailureListener { exception -> continuation.resumeWithException(exception) }
        }
    }

    suspend fun listAll(businessCode: String): List<ServiceOrder>? {
        return suspendCoroutine { continuation ->
            firestore.collection("businesses").document(businessCode).collection("orders").get()
                .addOnSuccessListener { querySnapshot -> continuation.resume(querySnapshot.toObjects()) }
                .addOnFailureListener { exception -> continuation.resumeWithException(exception) }
        }
    }

    suspend fun update(businessCode: String): List<ServiceOrder>? {
        return suspendCoroutine { continuation ->
            firestore.collection("businesses").document(businessCode).collection("orders").get()
                .addOnSuccessListener { querySnapshot -> continuation.resume(querySnapshot.toObjects()) }
                .addOnFailureListener { exception -> continuation.resumeWithException(exception) }
        }
    }

}
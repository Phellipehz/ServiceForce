package com.serviceforce.repository.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.serviceforce.models.Message
import com.serviceforce.models.ServiceOrder
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
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

    fun watchAll(businessCode: String) = callbackFlow<MutableList<ServiceOrder>> {
        var query = firestore.collection("businesses").document(businessCode).collection("orders")
        var subscription = query.addSnapshotListener { value, error ->
            if(error != null){
                if(!this.isClosedForSend) close(error)
            }else{
                if(!this.isClosedForSend) offer(value?.toObjects(ServiceOrder::class.java)!!)
            }
        }

        awaitClose { subscription.remove() }
    }

    suspend fun update(businessCode: String): List<ServiceOrder>? {
        return suspendCoroutine { continuation ->
            firestore.collection("businesses").document(businessCode).collection("orders").get()
                .addOnSuccessListener { querySnapshot -> continuation.resume(querySnapshot.toObjects()) }
                .addOnFailureListener { exception -> continuation.resumeWithException(exception) }
        }
    }

    suspend fun view(serviceOrderCode: String, businessCode: String): ServiceOrder? {
        return suspendCoroutine { continuation ->
            firestore.collection("businesses").document(businessCode).collection("orders").document(serviceOrderCode).get()
                .addOnSuccessListener { documentoSnapshot -> continuation.resume(documentoSnapshot.toObject<ServiceOrder>()) }
                .addOnFailureListener { exception -> continuation.resumeWithException(exception) }
        }
    }

}
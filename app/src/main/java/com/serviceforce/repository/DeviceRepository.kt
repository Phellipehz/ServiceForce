package com.serviceforce.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.serviceforce.models.Device
import com.serviceforce.models.User
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class DeviceRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun createUpdate(device: Device): Device? {
        return suspendCoroutine { continuation ->
            firestore.collection("devices").document(device.uuid).set(device, SetOptions.merge())
                .addOnSuccessListener { continuation.resume(device) }
                .addOnFailureListener { exception -> continuation.resumeWithException(exception) }
        }
    }

}
package com.serviceforce.repository.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.serviceforce.models.Message
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MessageRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun create(message: Message): Message = suspendCoroutine { continuation ->
        firestore.collection("messages").add(message).addOnSuccessListener { documentReference ->
            documentReference.get()
                .addOnSuccessListener {  documentSnapshot -> continuation.resume(documentSnapshot.toObject(Message::class.java)!!) }
                .addOnFailureListener { exception -> continuation.resumeWithException(exception) }
        }.addOnFailureListener { exception ->
            continuation.resumeWithException(exception)
        }
    }

    fun watch(userUID: String, userUidDestination: String) = callbackFlow<MutableList<Message>> {
        var query = firestore.collection("messages").whereEqualTo("userUidOrigin", userUID)
        var subscription = query.addSnapshotListener { value, error ->
            if(error != null){
                if(!this.isClosedForSend) close(error)
            }else{
                if(!this.isClosedForSend) offer(value?.toObjects(Message::class.java)!!)
            }
        }

        awaitClose { subscription.remove() }
    }



}
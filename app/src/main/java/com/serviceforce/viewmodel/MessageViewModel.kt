package com.serviceforce.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.serviceforce.models.Message
import com.serviceforce.repository.MessageRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel() {

    val messageMutableLiveData = MutableLiveData<Result<List<Message>>>()
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val messageRepository = MessageRepository()

    fun sendMessage(message: String, userUidDestination: String){
        if(firebaseAuth.currentUser?.uid == null){
            messageMutableLiveData.value = Result.failure(Exception("Não está logado"))
        }

        viewModelScope.launch {
            try {
                messageRepository.create(Message(firebaseAuth.currentUser!!.uid!!, userUidDestination, message))
            }catch (error: Exception){
                messageMutableLiveData.value = Result.failure(error)
            }
        }
    }

    fun watchMessage(userUidDestination: String){
        if(firebaseAuth.currentUser?.uid == null){
            messageMutableLiveData.value = Result.failure(Exception("Não está logado"))
        }

        viewModelScope.launch {
            try {
                messageRepository.watch(firebaseAuth.currentUser!!.uid!!, userUidDestination).collect { messages ->
                    messageMutableLiveData.value = Result.success(messages)
                }
            }catch (error: Exception){
                messageMutableLiveData.value = Result.failure(error)
            }
        }
    }

}
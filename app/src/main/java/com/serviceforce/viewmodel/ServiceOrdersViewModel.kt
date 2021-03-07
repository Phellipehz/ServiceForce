package com.serviceforce.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.serviceforce.ServiceForceApplication
import com.serviceforce.models.Message
import com.serviceforce.models.ServiceOrder
import com.serviceforce.repository.firebase.ServiceOrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ServiceOrdersViewModel : ViewModel() {

    val EXAMPLE_COUNTER = preferencesKey<Int>("example_counter")

    val dataStore: DataStore<Preferences> = ServiceForceApplication.context.createDataStore("service-force");
    val serviceOrdersMutableLiveData = MutableLiveData<Result<MutableList<ServiceOrder>>>()
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val serviceOrderRepository = ServiceOrderRepository()

    fun listAll(){
        if(firebaseAuth.currentUser?.uid == null){
            serviceOrdersMutableLiveData.value = Result.failure(Exception("Não está logado"))
        }


        viewModelScope.launch {
            try {
                val flowPreferences: Flow<Int> = dataStore.data.map { preferences -> preferences[EXAMPLE_COUNTER] ?: 0 }
                var businessCode = "";
                serviceOrderRepository.listAll(businessCode)
            }catch (error: Exception){
                serviceOrdersMutableLiveData.value = Result.failure(error)
            }
        }
    }

    fun watchRealtime(userUidDestination: String){
        if(firebaseAuth.currentUser?.uid == null){
            serviceOrdersMutableLiveData.value = Result.failure(Exception("Não está logado"))
        }

        viewModelScope.launch {
            try {
                var businessCode = "";

                val flowPreferences: Flow<Int> = dataStore.data.map { preferences -> preferences[EXAMPLE_COUNTER] ?: 0 }

                serviceOrderRepository.watchAll(businessCode).collect { value ->
                    serviceOrdersMutableLiveData.value = Result.success(value)
                }
            }catch (error: Exception){
                serviceOrdersMutableLiveData.value = Result.failure(error)
            }
        }
    }

}
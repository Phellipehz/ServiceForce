package com.serviceforce.viewmodel

import android.provider.Settings
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.serviceforce.ServiceForceApplication
import com.serviceforce.models.Device
import com.serviceforce.repository.AuthenticationRepository
import com.serviceforce.repository.DeviceRepository
import com.serviceforce.repository.UserRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class AuthenticationViewModel : ViewModel() {

    val dataStore: DataStore<Preferences> = ServiceForceApplication.context.createDataStore("service-force");

    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    val authenticationRepository = AuthenticationRepository()
    val userRepository = UserRepository()
    val deviceRepository = DeviceRepository()

    val authResultMutableLiveData = MutableLiveData<Result<AuthResult>>()
    val authenticatedResultMutableLiveData = MutableLiveData<Result<Boolean>>()

    fun authenticate(email: String, password: String){
        viewModelScope.launch {
            try {
                var androidID = Settings.Secure.getString(ServiceForceApplication.context.contentResolver, Settings.Secure.ANDROID_ID);

                var authenticationResult = authenticationRepository.authenticate(email, password)
                var user = userRepository.find(authenticationResult.user!!.uid)
                var device = deviceRepository.createUpdate(Device(androidID, Timestamp.now(), user!!.businessCode))

                val prefsKeys = preferencesKey<String>("businessCode")
                dataStore.edit { settings ->
                    settings[prefsKeys] = user!!.businessCode
                }

                authResultMutableLiveData.value = Result.success(authenticationResult)
            }catch (error: Exception){
                authResultMutableLiveData.value = Result.failure(error)
            }
        }
    }

    fun isAuthenticated(){
        authenticatedResultMutableLiveData.value = Result.success(firebaseAuth.currentUser != null)
    }

}
package com.serviceforce.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.serviceforce.R
import com.serviceforce.viewmodel.AuthenticationViewModel
import com.serviceforce.viewmodel.MessageViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var authenticationViewModel : AuthenticationViewModel
    lateinit var messageViewModel: MessageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        authenticationViewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)
        messageViewModel =  ViewModelProvider(this).get(MessageViewModel::class.java)
        setViewModelObservables()

        authenticationViewModel.authenticate("mail@mail.com", "mail123")
    }

    fun setViewModelObservables(){
        //viewLifecycleOwner
        authenticationViewModel.authResultMutableLiveData.observe(this, Observer { result ->
            if(result.isSuccess){
                hello.text = "Email: ${result.getOrNull()?.user?.email} UID: ${result.getOrNull()?.user?.uid}"
                Log.i("", "Email: ${result.getOrNull()?.user?.email}")
                sendAndWatchMessage()
            } else if (result.isFailure){
                Log.e("", result.exceptionOrNull().toString())
            }
        })

        messageViewModel.messageMutableLiveData.observe(this, Observer { result ->
            if(result.isSuccess){
                Log.i("", result.getOrNull().toString())
            } else if (result.isFailure){
                Log.e("", result.exceptionOrNull().toString())
            }
        })
    }

    fun sendAndWatchMessage(){
        messageViewModel.sendMessage("Mensagem 1", "USERIDDESTINATION")
        messageViewModel.watchMessage("USERIDDESTINATION")
    }


}
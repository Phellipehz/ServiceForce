package com.serviceforce.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.serviceforce.R
import com.serviceforce.viewmodel.AuthenticationViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity() {

    lateinit var authenticationViewModel : AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        authenticationViewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)

        setViewModelObservables()

        loginButton.setOnClickListener {
            var email = email.text.toString()
            var password = password.text.toString()
            authenticationViewModel.authenticate(email, password)
        }
    }

    fun setViewModelObservables(){
        //viewLifecycleOwner
        authenticationViewModel.authResultMutableLiveData.observe(this, Observer { result ->
            if(result.isSuccess){
               // hello.text = "Email: ${result.getOrNull()?.user?.email} UID: ${result.getOrNull()?.user?.uid}"
                Log.i("", "Email: ${result.getOrNull()?.user?.email}")
            } else if (result.isFailure){
                Snackbar.make(loginButton, "Problema ao logar, tente mais tarde", Snackbar.LENGTH_INDEFINITE).show()
            }
        })

    }

}
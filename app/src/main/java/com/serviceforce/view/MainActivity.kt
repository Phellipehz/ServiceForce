package com.serviceforce.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.serviceforce.R
import com.serviceforce.viewmodel.AuthenticationViewModel
import com.serviceforce.viewmodel.MessageViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var authenticationViewModel : AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        authenticationViewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)
        authenticationViewModel.isAuthenticated()
        authenticationViewModel.authenticatedResultMutableLiveData.observe(this, Observer { result ->
            if(result.isSuccess){
                if(result.getOrNull()!!){
                    startActivity(Intent(this, LoginActivity::class.java))
                }else{
                    startActivity(Intent(this, ServiceOrderListActivity::class.java))
                }
            } else if (result.isFailure){
                Snackbar.make(loginButton, "Problema ao logar, tente mais tarde", Snackbar.LENGTH_INDEFINITE).show()
            }
        })

    }

}
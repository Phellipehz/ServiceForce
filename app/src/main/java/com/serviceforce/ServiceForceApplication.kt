package com.serviceforce

import android.app.Application
import android.content.Context
import java.net.ContentHandler

class ServiceForceApplication: Application() {

    companion object {
        lateinit var context: Context;
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }

}
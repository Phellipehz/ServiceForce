package com.serviceforce.models

data class Message(val userUidOrigin: String, val userUidDestination: String, val message: String){
    constructor() : this(userUidOrigin="", userUidDestination="", message="")
}
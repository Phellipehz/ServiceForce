package com.serviceforce.models

data class ServiceOrder(val userUidOrigin: String, val userUidDestination: String, val message: String){
    constructor() : this(userUidOrigin="", userUidDestination="", message="")
}
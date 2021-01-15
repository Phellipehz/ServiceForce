package com.serviceforce.models

import com.google.firebase.Timestamp

data class Device(val uuid: String, val createdAt: Timestamp?, val businessCode: String){
    constructor() : this(uuid="", createdAt=null, businessCode="")
}
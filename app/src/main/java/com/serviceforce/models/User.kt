package com.serviceforce.models

data class User(val uid: String, val mail: String, val businessCode: String){
    constructor() : this(uid="", mail="", businessCode = "")
}
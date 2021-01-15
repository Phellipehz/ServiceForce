package com.serviceforce.models

import com.google.firebase.Timestamp

data class ServiceOrder(
    val orderNumber: String,
    val hardwareDescription: String,
    val requesterCode: String,
    val claimedFault: String,
    val detectedFault: String,
    val faultSolution: String,
    val status: String, // (Entrada, Programada, Iniciada, Parada, Finalizada, Cancelada)
    val priority: String, // LOW, MEDIUM, HIGH, NOW
    val entryDate: Timestamp?,
    val expectedDeliveryDate: Timestamp?,
    val deliveryDate: Timestamp?,
    val notes: String
){
    constructor() : this(
        orderNumber = "", hardwareDescription = "", requesterCode = "", claimedFault = "",
        detectedFault = "", faultSolution = "", status = "", priority = "", entryDate = null, expectedDeliveryDate = null, deliveryDate = null, notes = ""
    )
}
package com.moviles.restaurante.models

data class ResponseGeneric<T>(
    val res:String,
    val data:T,
    val message:String?
)
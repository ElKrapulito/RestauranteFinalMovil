package com.moviles.restaurante.models

import java.io.Serializable

data class Usuario(
    val id:Int,
    val usuario:String,
    val password:String?,
    val tipoUsuario: String?,
    val restaurantes: ArrayList<Restaurant>?,
    val token:String?
): Serializable
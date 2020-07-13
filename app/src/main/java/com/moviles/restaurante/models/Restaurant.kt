package com.moviles.restaurante.models

data class Restaurant (
    val id: Int,
    val nombre:String,
    val direccion:String,
    val latitud:String,
    val longitud:String,
    val usuario_id:String,
    val productos: ArrayList<Producto>?
)
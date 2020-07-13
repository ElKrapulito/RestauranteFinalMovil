package com.moviles.restaurante.models

import java.io.Serializable

data class Producto(
    val id: Int,
    var nombre: String,
    var descripcion: String,
    val restaurante_id:String,
    var precio: String,
    var producto_id: Int?,
    var precioProducto: String?,
    var cantidad:Int
):Serializable
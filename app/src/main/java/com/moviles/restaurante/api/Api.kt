package com.moviles.restaurante.api

import com.moviles.restaurante.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface Api {

    @GET("restaurantes/{id}")
    suspend fun getRestaurant(@Path("id") id:Int): ResponseGeneric<Restaurant>

    @GET("usuarios/{id}")
    suspend fun getUserDetails(@Path("id") id:Int): ResponseGeneric<Usuario>

    @GET("restaurantes/{id}/pedidos")
    suspend fun getOrders(@Path("id") id: Int): ResponseGeneric<ArrayList<Pedido>>

    @GET("restaurantes/{id}.jpg")
    fun getRestaurantImage(@Path("id") id: Int):Call<ResponseGeneric<Restaurant>>

    @POST("usuarios/login")
    suspend fun login(@Body usuario: Usuario): ResponseGeneric<ArrayList<Usuario>>

    @POST("productos")
    fun insertProduct(@Body product: Producto):Call<ResponseGeneric<Producto>>

    @POST("productos/{id}")
    fun updateProduct(@Path("id") id: Int, @Body product: Producto):Call<ResponseGeneric<Producto>>

    @POST("pedidos/{id}/{state}")
    fun changeState(@Path("id") id: Int,@Path("state") state:String):Call<ResponseGeneric<Pedido>>

    @POST("productos/{id}/eliminar")
    fun deleteProduct(@Path("id") id: Int): Call<ResponseGeneric<String>>

    @Multipart
    @POST("productos/{id}/foto")
    fun uploadImageProduct(
        @Path("id")id: Int,
        @Part image: MultipartBody.Part?
    ): Call<ResponseGeneric<Producto>>
}
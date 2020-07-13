package com.moviles.restaurante.activities.box

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.moviles.restaurante.activities.MainActivity
import com.moviles.restaurante.api.Api
import com.moviles.restaurante.models.Pedido
import com.moviles.restaurante.models.ResponseGeneric
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.ClassCastException

class ChangeStateBox(private val order: Pedido): DialogFragment() {
    internal lateinit var listener: MyDialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as MyDialogListener
        } catch (e:ClassCastException){
            throw ClassCastException((context.toString() +
                    " must implement NoticeDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction

            val builder = AlertDialog.Builder(it)
            builder.setTitle("Escoger estado")
                .setItems(
                    arrayOf("En Cocina","Despachado")
                ) { _, which ->
                    if(which == 0){
                        changeState(order.id,"encocina")
                    } else if(which == 1){
                        changeState(order.id,"despachado")
                    }
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun changeState(orderId:Int, state:String){
        val retrofit = Retrofit.Builder()
            .baseUrl(MainActivity.apiURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val caseService = retrofit.create(Api::class.java)
        val request = caseService.changeState(orderId,state)
        request.enqueue(object : Callback<ResponseGeneric<Pedido>>{
            override fun onFailure(call: Call<ResponseGeneric<Pedido>>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<ResponseGeneric<Pedido>>,
                response: Response<ResponseGeneric<Pedido>>
            ) {
                if (response.body()?.res == "success"){
                    Log.d("BoxState", "Estate Changed!")
                    val order = response.body()!!.data
                    listener.onOrderChanged(order)
                }
            }

        })

    }

    interface MyDialogListener {
        fun onOrderChanged(pedido: Pedido)
    }


}
package com.moviles.restaurante.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.restaurantefinal.R
import com.moviles.restaurante.api.Api
import com.moviles.restaurante.models.ResponseGeneric
import com.moviles.restaurante.models.Usuario
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var token:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("MainActivity", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                token = task.result?.token.toString()

            })

        setUpListener()
    }

    private fun setUpListener(){

        btnLogin.setOnClickListener {
            Log.d("MainActivity", token)
            var usuario = Usuario(
                0,
                txtUsername.text.toString(),
                txtPass.text.toString(),
                null,
                null,
                token
            )
            val result = login(usuario)



            if(result.res.equals("success")){
                usuario = result.data[0]
                val intent = Intent(this, RestaurantsActivity::class.java)
                intent.putExtra("usuario", usuario)
                startActivity(intent)
            }
        }
    }

    private fun login(usuario: Usuario): ResponseGeneric<ArrayList<Usuario>> {
        val retrofit = Retrofit.Builder()
            .baseUrl(apiURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val caseService = retrofit.create(Api::class.java)
        val request: ResponseGeneric<ArrayList<Usuario>>
        runBlocking {
            request = caseService.login(usuario)
            Log.d("MainActivity",request.data[0].usuario)
        }
        return request
    }

    companion object {
        const val apiURL = "http://delivery.jmacboy.com/api/"
    }

}

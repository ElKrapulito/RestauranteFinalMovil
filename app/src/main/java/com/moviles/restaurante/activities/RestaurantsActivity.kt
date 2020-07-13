package com.moviles.restaurante.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.restaurantefinal.R
import com.moviles.restaurante.fragments.RestaurantsFragment
import com.moviles.restaurante.models.Restaurant
import com.moviles.restaurante.models.Usuario

class RestaurantsActivity : AppCompatActivity(),
    RestaurantsFragment.OnListFragmentInteractionListener {

    private lateinit var usuario: Usuario
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurants)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        usuario = intent.getSerializableExtra("usuario") as Usuario

        val fragment = RestaurantsFragment.newInstance(usuario.id)
        fragmentTransaction.add(R.id.frameRestaurant, fragment)

        fragmentTransaction.commit()

    }

    override fun onListFragmentInteraction(item: Restaurant?) {
        val intent = Intent(this, ProductManagmentActivity::class.java)
        intent.putExtra("restaurantId", item?.id)
        intent.putExtra("userId", usuario.id)
        startActivity(intent)
    }


}

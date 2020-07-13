package com.moviles.restaurante.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.restaurantefinal.R
import com.moviles.restaurante.api.Api
import com.moviles.restaurante.fragments.ProductFragment
import com.moviles.restaurante.models.Producto
import com.moviles.restaurante.models.ResponseGeneric
import kotlinx.android.synthetic.main.activity_product_managment.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class ProductManagmentActivity : AppCompatActivity(), ProductFragment.OnListFragmentInteractionListener {

    private var userId = 0
    private var restaurantId = 0
    private lateinit var fragment: ProductFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_managment)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        userId = intent.getIntExtra("userId",0)
        restaurantId = intent.getIntExtra("restaurantId",0)

        fragment = ProductFragment.newInstance(restaurantId)
        fragmentTransaction.add(R.id.frameProducts, fragment)

        fragmentTransaction.commit()
        setUpListeners()
    }

    override fun onListFragmentInteraction(item: Producto?) {
        val intent = Intent(this, ProductFormActivity::class.java)
        intent.putExtra("product", item)
        startActivity(intent)
    }

    override fun onDeleteProduct(item: Producto?) {
        if(item != null){
            fragment.deleteProduct(item)
            deleteProduct(item)
        }
    }

    private fun deleteProduct(product:Producto){
        val retrofit = Retrofit.Builder()
            .baseUrl(MainActivity.apiURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(Api::class.java)
        val request = api.deleteProduct(product.id)
        request.enqueue(object : Callback<ResponseGeneric<String>>{
            override fun onFailure(call: Call<ResponseGeneric<String>>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<ResponseGeneric<String>>,
                response: Response<ResponseGeneric<String>>
            ) {
                Toast.makeText(this@ProductManagmentActivity, "Product Deleted!", Toast.LENGTH_LONG).show()
            }

        })
    }

    private  fun setUpListeners(){
        btnCreateProduct.setOnClickListener {
            val intent = Intent(this,
                ProductFormActivity::class.java)
            intent.putExtra("restaurantId",restaurantId)
            startActivityForResult(intent,0)
        }

        btnOrders.setOnClickListener {
            val intent = Intent(this,
                OrderActivity::class.java)
            intent.putExtra("restaurantId",restaurantId)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 0){
            /*val product = data?.getSerializableExtra("product") as Producto
            fragment.updateProducts(product)*/
            finish()
            startActivity(intent)
        }

    }
}

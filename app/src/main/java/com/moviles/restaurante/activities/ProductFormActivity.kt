package com.moviles.restaurante.activities

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.content.CursorLoader
import com.example.restaurantefinal.R
import com.moviles.restaurante.api.Api
import com.moviles.restaurante.models.Producto
import com.moviles.restaurante.models.ResponseGeneric
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_form.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


class ProductFormActivity : AppCompatActivity() {

    private var restaurantId = 0
    private lateinit var product: Producto
    private lateinit var imgUri:Uri
    private var upload:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_form)

        restaurantId = intent.getIntExtra("restaurantId",0)

        if(restaurantId == 0){
            product = intent.getSerializableExtra("product") as Producto

            txtProductName.setText(product.nombre)
            txtProductDescription.setText(product.descripcion)
            txtProductPrice.setText(product.precio)
            Picasso.get().load("http://delivery.jmacboy.com/img/productos/${product.id}.jpg").into(imgUpload)

        }

        setUpListeners()
    }

    private fun setUpListeners(){
        btnSaveProduct.setOnClickListener {
            val name = txtProductName.text.toString()
            val description =  txtProductDescription.text.toString()
            val price = txtProductPrice.text.toString()

            if(restaurantId == 0){
                product.nombre = name
                product.descripcion = description
                product.precio = price
                updateProduct(product)
            } else {
                val product = Producto(
                    0,
                    name,
                    description,
                    restaurantId.toString(),
                    price,
                    null,
                    null,
                    0
                )
                insertProduct(product)
            }
        }

        btnSelectImg.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
            upload = true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE) {
            Toast.makeText(this, "${data?.data}", Toast.LENGTH_LONG).show()
            imgUpload.setImageURI(null)
            if(data?.data != null && data.data is Uri){
                imgUri = data.data!!
                imgUpload.setImageURI(null)
                imgUpload.setImageURI(data.data)
            }
        }
    }



    private fun insertProduct(product: Producto) {
        val retrofit = Retrofit.Builder()
            .baseUrl(MainActivity.apiURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val caseService = retrofit.create(Api::class.java)
        val request = caseService.insertProduct(product)
        request.enqueue(object : Callback<ResponseGeneric<Producto>>{
            override fun onFailure(call: Call<ResponseGeneric<Producto>>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<ResponseGeneric<Producto>>,
                response: Response<ResponseGeneric<Producto>>
            ) {
                if(response.body()?.res == "success"){
                    val file = File(getRealPathFromURI(imgUri)!!)
                    val requestFile: RequestBody =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file)
                    val body =
                        MultipartBody.Part.createFormData("image", file.name, requestFile)
                    val productito = response.body()!!.data
                    uploadImage(productito, body)

                    val intent = Intent(this@ProductFormActivity, ProductManagmentActivity::class.java)
                    intent.putExtra("product", response.body()!!.data)
                    setResult(0, intent)
                    finish()
                }
            }

        })
    }

    private fun uploadImage(product: Producto, image: MultipartBody.Part){
        val retrofit = Retrofit.Builder()
            .baseUrl(MainActivity.apiURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(Api::class.java)
        val request = api.uploadImageProduct(product.id, image)
        request.enqueue(object : Callback<ResponseGeneric<Producto>>{
            override fun onFailure(call: Call<ResponseGeneric<Producto>>, t: Throwable) {
                Log.d("FormProduct","Error uploading image: ${t.message}")
            }
            override fun onResponse(
                call: Call<ResponseGeneric<Producto>>,
                response: Response<ResponseGeneric<Producto>>
            ) {
                Log.d("FormProduct", "Image Uploaded!")
            }
        })
    }


    private fun updateProduct(product: Producto) {
        val retrofit = Retrofit.Builder()
            .baseUrl(MainActivity.apiURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val caseService = retrofit.create(Api::class.java)
        val request = caseService.updateProduct(product.id,product)
        request.enqueue(object : Callback<ResponseGeneric<Producto>>{
            override fun onFailure(call: Call<ResponseGeneric<Producto>>, t: Throwable) {

            }
            override fun onResponse(
                call: Call<ResponseGeneric<Producto>>,
                response: Response<ResponseGeneric<Producto>>
            ) {
                if(response.body()?.res == "success"){
                    if(upload){
                        val file = File(getRealPathFromURI(imgUri)!!)
                        val requestFile: RequestBody =
                            RequestBody.create(MediaType.parse("multipart/form-data"), file)
                        val body =
                            MultipartBody.Part.createFormData("image", file.name, requestFile)
                        val productito = response.body()!!.data
                        uploadImage(productito, body)
                    }

                    val intent = Intent(this@ProductFormActivity, ProductManagmentActivity::class.java)
                    intent.putExtra("product", response.body()!!.data)
                    setResult(1, intent)
                    finish()
                }
            }

        })
    }

    private fun getRealPathFromURI(contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(this, contentUri, proj, null, null, null)
        val cursor: Cursor = loader.loadInBackground()!!
        val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result: String = cursor.getString(column_index)
        cursor.close()
        return result
    }

    companion object{
        const val PICK_IMAGE = 1
    }
}

package com.moviles.restaurante.activities


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.restaurantefinal.R
import com.moviles.restaurante.activities.box.ChangeStateBox
import com.moviles.restaurante.fragments.OrderFragment
import com.moviles.restaurante.models.Pedido

class OrderActivity : AppCompatActivity(), OrderFragment.OnListFragmentInteractionListener,
    ChangeStateBox.MyDialogListener {

    private lateinit var fragment: OrderFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val restaurantId = intent.getIntExtra("restaurantId",0)

        fragment = OrderFragment.newInstance(restaurantId)
        fragmentTransaction.add(R.id.frameOder, fragment)

        fragmentTransaction.commit()
    }

    override fun onListFragmentInteraction(item: Pedido?) {
        if(item != null){
            ChangeStateBox(item)
                .show(supportFragmentManager,"Pedidos")
        }
    }

    override fun onOrderChanged(pedido: Pedido) {
        finish()
        startActivity(intent)
        //fragment.updateOrders(pedido)
        //Toast.makeText(this,"Update?", Toast.LENGTH_LONG).show()
    }

}

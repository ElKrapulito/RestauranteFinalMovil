package com.moviles.restaurante.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.restaurantefinal.R
import com.moviles.restaurante.activities.MainActivity
import com.moviles.restaurante.api.Api
import com.moviles.restaurante.fragments.adapters.MyProductRecyclerViewAdapter
import com.moviles.restaurante.models.Producto
import com.moviles.restaurante.models.ResponseGeneric
import com.moviles.restaurante.models.Restaurant
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ProductFragment.OnListFragmentInteractionListener] interface.
 */
class ProductFragment : Fragment() {

    // TODO: Customize parameters
    private var restaurantId = 1
    private lateinit var products: ArrayList<Producto>
    private lateinit var mAdapter: MyProductRecyclerViewAdapter
    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
        restaurantId = it.getInt(ARG_RESTAURANT_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product_list, container, false)
        products = getProducts(restaurantId)
        mAdapter =
            MyProductRecyclerViewAdapter(
                products,
                listener
            )
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager =
                    LinearLayoutManager(context)
                adapter = mAdapter

            }
        }
        return view
    }

    private fun getProducts(restaurantId: Int): ArrayList<Producto> {
        val retrofit = Retrofit.Builder()
            .baseUrl(MainActivity.apiURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val caseService = retrofit.create(Api::class.java)
        val request: ResponseGeneric<Restaurant>
        runBlocking {
            request = caseService.getRestaurant(restaurantId)
        }
        return request.data.productos!!
    }

    fun deleteProduct(product: Producto){
        products.remove(product)
        mAdapter.notifyDataSetChanged()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: Producto?)
        fun onDeleteProduct(item: Producto?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_RESTAURANT_ID = "restaurantId"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(restaurantId: Int) =
            ProductFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_RESTAURANT_ID, restaurantId)
                }
            }
    }
}

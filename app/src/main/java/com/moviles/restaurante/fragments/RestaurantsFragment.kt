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
import com.moviles.restaurante.fragments.adapters.MyRestaurantsRecyclerViewAdapter
import com.moviles.restaurante.models.ResponseGeneric
import com.moviles.restaurante.models.Restaurant
import com.moviles.restaurante.models.Usuario
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [RestaurantsFragment.OnListFragmentInteractionListener] interface.
 */
class RestaurantsFragment : Fragment() {

    // TODO: Customize parameters
    private var usuarioId = 1
    private lateinit var restaurants: ArrayList<Restaurant>

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            usuarioId = it.getInt(ARG_USER_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_restaurants_list, container, false)
        restaurants = getRestaurants(usuarioId)
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager =
                    LinearLayoutManager(context)


                adapter =
                    MyRestaurantsRecyclerViewAdapter(
                        restaurants,
                        listener
                    )
            }
        }
        return view
    }

    private fun getRestaurants(usuarioId: Int): ArrayList<Restaurant> {
        val retrofit = Retrofit.Builder()
            .baseUrl(MainActivity.apiURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val caseService = retrofit.create(Api::class.java)
        val request: ResponseGeneric<Usuario>
        runBlocking {
            request = caseService.getUserDetails(usuarioId)
        }
        return request.data.restaurantes!!
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
        fun onListFragmentInteraction(item: Restaurant?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_USER_ID = "usuarioId"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(usuarioId: Int) =
            RestaurantsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_USER_ID, usuarioId)
                }
            }
    }
}

package com.moviles.restaurante.fragments.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.restaurantefinal.R


import com.moviles.restaurante.fragments.ProductFragment.OnListFragmentInteractionListener
import com.moviles.restaurante.models.Producto
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.fragment_product.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyProductRecyclerViewAdapter(
    private val mValues: List<Producto>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyProductRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private val imageButtonListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Producto
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)

        }

        imageButtonListener = View.OnClickListener {
            val item = it.tag as Producto
            mListener?.onDeleteProduct(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mContentView.text = item.nombre
        Picasso.get().load("http://delivery.jmacboy.com/img/productos/${item.id}.jpg").into(holder.mImg)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }

        with(holder.mView.btnDeleteProduct){
            tag = item
            setOnClickListener(imageButtonListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mContentView: TextView = mView.content
        val mDelete: ImageButton = mView.btnDeleteProduct
        val mImg:ImageView = mView.imgProduct
    }
}

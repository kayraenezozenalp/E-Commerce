package com.io1.e_commerce.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.io1.e_commerce.HomeFragmentDirections
import com.io1.e_commerce.R
import com.io1.e_commerce.model.FirebaseProductsResponse
import com.io1.e_commerce.model.FirebaseResponse
import com.squareup.picasso.Picasso
import kotlinx.coroutines.NonDisposableHandle.parent

class HomeAdapter(var productList: ArrayList<FirebaseResponse>): RecyclerView.Adapter<HomeAdapter.ProductViewHolder>() {

    private var onClickListener: OnClickListener? = null

    class ProductViewHolder(var view : View) : RecyclerView.ViewHolder(view) {
        val nameText : TextView
        val priceText : TextView
        val productImage : ImageView


        init {
            nameText = view.findViewById(R.id.productName_TextView)
            priceText = view.findViewById(R.id.productPrice)
            productImage = view.findViewById(R.id.productImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_product,parent,false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            val item = productList[position]
            holder.nameText.text = productList[position].ProductName
            holder.priceText.text = productList[position].ProductPrice
            val id = productList[position].ProductID

            holder.view.setOnClickListener(object : View.OnClickListener{
                override fun onClick(p0: View?) {
                    val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(productList[holder.adapterPosition].ProductName,productList[holder.adapterPosition].ProductPrice,productList[holder.adapterPosition].ProductStock,productList[holder.adapterPosition].ProductImage,
                        id.toString(),true
                    )
                    productList.clear()
                    holder.view.findNavController().navigate(action)
                }
            })

          Picasso.with(holder.view.context)
              .load(productList[position].ProductImage)
             .into(holder.productImage)

    }

    fun updateProductList(newProductList: List<FirebaseResponse>){
        productList.clear()
        productList.addAll(newProductList)
        notifyDataSetChanged()
    }

    fun searchDataList(searchList: List<FirebaseResponse>){
        productList.addAll(searchList)
        notifyDataSetChanged()
    }

    fun setOnClickListener(onClickListener: OnClickListener ) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: FirebaseResponse)
    }
}
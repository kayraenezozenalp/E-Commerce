package com.io1.e_commerce.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.io1.e_commerce.BasketRetriver
import com.io1.e_commerce.HomeFragmentDirections
import com.io1.e_commerce.R
import com.io1.e_commerce.model.BasketItems
import com.io1.e_commerce.model.FirebaseResponse
import com.squareup.picasso.Picasso

class BasketAdapter(var shoppingList : List<FirebaseResponse>): RecyclerView.Adapter<BasketAdapter.BasketViewHolder>(){

    class BasketViewHolder(var view : View) : RecyclerView.ViewHolder(view){
        val nameText : TextView
        val priceText : TextView
        val productImage : ImageView
        val productAmount : TextView
        val addImage : ImageView
        val remove : ImageView
        val delete : ImageView

        init {
            nameText = view.findViewById(R.id.favProductName)
            priceText = view.findViewById(R.id.cartProductPrice)
            productImage = view.findViewById(R.id.cartProductImage)
            addImage = view.findViewById(R.id.productQuantityPlus)
            remove = view.findViewById(R.id.productQuantityMinus)
            delete = view.findViewById(R.id.deleteItemFromCart)
            productAmount = view.findViewById(R.id.productQuantity_EditText)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.basket_item,parent,false)
        return BasketViewHolder(view)
    }

    override fun getItemCount(): Int {
        return shoppingList.size
    }

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        val item = shoppingList[position]
        holder.productAmount.text = shoppingList[position].ProductAmount.toString()
        holder.nameText.text = shoppingList[position].ProductName
        holder.priceText.text = shoppingList[position].ProductPrice

        holder.addImage.setOnClickListener {
                holder.productAmount.text = (shoppingList[position].ProductAmount + 1).toString()
                BasketRetriver.values  +=  shoppingList[position].ProductPrice.toInt()

                shoppingList[position].ProductAmount =  shoppingList[position].ProductAmount + 1
        }

        holder.delete.setOnClickListener {
            val deletedProduct = shoppingList[position]

            // Silinecek öğeyi hariç tutarak yeni bir liste oluşturun
            val updatedList = shoppingList.filterIndexed { index, _ -> index != position }

            // Değiştirme işlemi
            shoppingList = updatedList

            // Sepet değerlerini güncelleyin
            BasketRetriver.values -= deletedProduct.ProductAmount * deletedProduct.ProductPrice.toInt()

            // RecyclerView'i güncelle
            notifyDataSetChanged()
        }

        holder.remove.setOnClickListener {
            holder.productAmount.text = (shoppingList[position].ProductAmount - 1).toString()
            BasketRetriver.values -= (shoppingList[position].ProductAmount -1) * shoppingList[position].ProductPrice.toInt()
            shoppingList[position].ProductAmount =  shoppingList[position].ProductAmount - 1
        }



        Picasso.with(holder.view.context)
            .load(shoppingList[position].ProductImage)
            .into(holder.productImage)
    }
}
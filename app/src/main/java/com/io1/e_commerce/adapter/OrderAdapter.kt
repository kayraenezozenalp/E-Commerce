package com.io1.e_commerce.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.io1.e_commerce.R
import com.io1.e_commerce.model.FirebaseResponse
import com.io1.e_commerce.model.OrderItems

class OrderAdapter(val orderList : List<OrderItems>): RecyclerView.Adapter<OrderAdapter.OrderViewHolder>(){

    class OrderViewHolder (var view : View) : RecyclerView.ViewHolder(view) {
        val orderNum : TextView
        val orderDate : TextView
        val orderLocation : TextView
        val orderProducts : TextView

        init {
            orderNum = view.findViewById(R.id.orderNum)
            orderDate = view.findViewById(R.id.orderDate)
            orderLocation = view.findViewById(R.id.orderLocation)
            orderProducts = view.findViewById(R.id.orderProducts)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.order_item,parent,false)
        return OrderAdapter.OrderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.orderNum.text = orderList[position].orderNum.toString()
        holder.orderDate.text = orderList[position].OrderData.toString()
        holder.orderLocation.text = orderList[position].orderLocation.toString()
        holder.orderProducts.text = orderList[position].orderProducts.toString()
    }

}



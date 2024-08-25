package com.io1.e_commerce

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.io1.e_commerce.adapter.BasketAdapter
import com.io1.e_commerce.adapter.OrderAdapter
import com.io1.e_commerce.databinding.FragmentOrderBinding
import com.io1.e_commerce.databinding.FragmentUpdateBinding
import com.io1.e_commerce.model.FirebaseResponse
import com.io1.e_commerce.model.OrderItems
import com.squareup.picasso.Picasso
import java.util.UUID

class OrderFragment : Fragment() {

    private lateinit var binding: FragmentOrderBinding
    private lateinit var firebaseAuth : FirebaseAuth
    lateinit var orderDate : String
    lateinit var orderLocation : String
    var orderProducts : Int = 10
    var retrievedValues: List<OrderItems> = mutableListOf()
    private lateinit var newRecyclerView: RecyclerView

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderBinding.inflate(inflater,container,false)
        val view = binding.root

        firebaseAuth = FirebaseAuth.getInstance()

        newRecyclerView = binding.allOrdersRV
        newRecyclerView.layoutManager = LinearLayoutManager(this.context)
        newRecyclerView.setHasFixedSize(true)


        db.collection("orders")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    System.out.println("aaaaaaaaaaaaaa")
                 val orderPrice =   System.out.println(document.getString("OrderPrice")).toString()
                 val orderAddress =   System.out.println(document.getString("OrderAddress")).toString()
                 val orderData =   System.out.println(document.getString("OrderData")).toString()
                    System.out.println(document.id)
                    System.out.println(document.data)
                    val order = OrderItems("sds",orderData,orderAddress,orderPrice)
                    OrderHolder.addValue(order)
                   System.out.println("retrievValue " + DataRetrieverr.getValues())
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }


       // getUserData()

        return view


    }

    object OrderHolder {
        var values: MutableList<OrderItems> = mutableListOf()

        fun addValue(value: OrderItems) {
            values.add(value)
            System.out.println(values.toList())
        }

    }
    fun getUserData() {
        newRecyclerView.adapter = OrderAdapter(retrievedValues.toList())
        System.out.println("adapter" +retrievedValues)
        newRecyclerView.adapter?.notifyDataSetChanged()
    }

    object DataRetrieverr {
        fun getValues(): List<OrderItems> {
            return OrderHolder.values.toList()
        }
    }


}


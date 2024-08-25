package com.io1.e_commerce

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.io1.e_commerce.adapter.BasketAdapter
import com.io1.e_commerce.adapter.HomeAdapter
import com.io1.e_commerce.adapter.SearchAdapter
import com.io1.e_commerce.databinding.FragmentPaymentBinding
import com.io1.e_commerce.model.FirebaseResponse
import com.io1.e_commerce.viewmodel.HomeViewModel


class SearchFragment : Fragment() {

    private lateinit var binding : FragmentPaymentBinding
    private lateinit var viewModel: HomeViewModel

    private lateinit var newRecyclerView: RecyclerView
    private val searchAdapter = HomeAdapter(arrayListOf())
    val SearchList = ArrayList<FirebaseResponse>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = FragmentPaymentBinding.inflate(inflater,container,false)
        val view = binding.root

        newRecyclerView = binding.searchList
        newRecyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        newRecyclerView.setHasFixedSize(true)
        newRecyclerView.adapter = searchAdapter

        searchAdapter.updateProductList(NewProductListHolder.getList())



        //   getUserData()
    /*    viewModel.products.observe(viewLifecycleOwner, Observer { product ->
            product?.let {
                System.out.println("sadsad")
                System.out.println(product)
                searchAdapter.updateProductList(NewProductListHolder.getList())
                newRecyclerView.adapter?.notifyDataSetChanged()
            }
        })

     */


        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != ""){
                    SearchList.clear()
                    for(search in NewProductListHolder.getList()){
                        if(search.ProductName.lowercase().contains(newText!!.lowercase()) == true){
                            SearchList.add(search)
                        }
                    }
                    searchAdapter.updateProductList(SearchList)
                }else{
                    searchAdapter.updateProductList(NewProductListHolder.getList())
                }
                return true
            }

        })
        return view
    }

    fun getUserData() {
        System.out.println("products list " +viewModel.foreignProducttList)

    }

}
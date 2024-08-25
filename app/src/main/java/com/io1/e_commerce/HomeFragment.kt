package com.io1.e_commerce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.io1.e_commerce.adapter.HomeAdapter
import com.io1.e_commerce.adapter.TurkishProductAdapter
import com.io1.e_commerce.databinding.FragmentHomeBinding
import com.io1.e_commerce.model.FirebaseResponse
import com.io1.e_commerce.service.foreign
import com.io1.e_commerce.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel
    private val productAdapter = HomeAdapter(arrayListOf())
    private val turkishProductAdapter = TurkishProductAdapter(arrayListOf())
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var turkishRecyclerView: RecyclerView
    private lateinit var binding : FragmentHomeBinding
    val products = MutableLiveData<List<FirebaseResponse>>()
    var productName = ""
    var productPrice = ""
    var imageURL : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val BaseUrl = "https://firestore.googleapis.com/v1/projects/e-commerce-4c753/databases/(default)/documents/"
        val db = Firebase.firestore
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        val view = binding.root
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.refreshData()
        newRecyclerView = binding.foreignproductList
        newRecyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        newRecyclerView.setHasFixedSize(true)
        newRecyclerView.adapter = productAdapter



        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


     /*   val apiService = retrofit.create(foreign::class.java)
            lifecycleScope.launch {
            val response = apiService.service()

              System.out.println(response)
              for(document in response.documents)  {
                  productName = document.fields.ProductName.stringValue
                  productPrice = document.fields.ProductPrice.stringValue
                  System.out.println(productName)

              }
                   //binding.imageView.
        }
      */

        imageURL ="https://images.samsung.com/is/image/samsung/assets/ae/tvs/2023_VD_TVs_All-TV-Categories_01_Mo1.jpg"

//        db.collection("Turkish")
//            .get()
//            .addOnSuccessListener { result ->
//                for (document in result){
//                    System.out.println("${document.id} => ${document.data}")
//                }
//            }
        observeLiveData()
        return view
    }



    fun observeLiveData(){
        viewModel.products.observe(viewLifecycleOwner, Observer { product ->
            product?.let {
                binding.foreignproductList.visibility = View.VISIBLE
                NewProductListHolder.addValue(product)

            }
            productAdapter.updateProductList(product)
        })

        viewModel.productError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if(it){
                    Toast.makeText(requireContext(),"Network Connection Error",Toast.LENGTH_LONG).show()
                }
            }
        })
        viewModel.productLoading.observe(viewLifecycleOwner, Observer {loading ->
            loading?.let {
                if(it){
                    binding.foreignproductList.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                    binding.textView4.visibility = View.GONE
                }else{
                    binding.foreignproductList.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    binding.textView4.visibility = View.VISIBLE
                }
            }

        })
    }
}
object NewProductListHolder {
    var values: MutableList<FirebaseResponse> = mutableListOf()

    fun addValue(newValues: List<FirebaseResponse>) {
        values.clear()
        values.addAll(newValues)
    }

    fun addOrUpdateValue(value: FirebaseResponse) {
        val existingIndex =
            values.indexOfFirst { it.ProductID == value.ProductID }
        if (existingIndex != -1) {
            values[existingIndex] = value
        } else {
            values.add(value)
        }
    }

    fun removeValue(value: FirebaseResponse) {
        val existingIndex =
            values.indexOfFirst { it.ProductID == value.ProductID }
        if (existingIndex != -1) {
            values.removeAt(existingIndex)
        }
    }
    fun getList(): List<FirebaseResponse> {
        return values.toList()
    }
}
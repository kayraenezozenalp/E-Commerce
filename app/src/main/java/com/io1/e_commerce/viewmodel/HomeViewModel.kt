package com.io1.e_commerce.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.io1.e_commerce.model.FirebaseAllProductsResponse
import com.io1.e_commerce.model.FirebaseProductsResponse
import com.io1.e_commerce.model.FirebaseResponse
import com.io1.e_commerce.service.ProductApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class HomeViewModel : ViewModel() {
    val products = MutableLiveData<List<FirebaseResponse>>()
    val trProducts = MutableLiveData<List<FirebaseResponse>>()
    val productError = MutableLiveData<Boolean>()
    val productLoading = MutableLiveData<Boolean>()
    val productList = products.value?.toMutableList() ?: mutableListOf()
    val foreignProducttList = arrayListOf<FirebaseResponse>()
    val trProducttList = arrayListOf<FirebaseResponse>()
    val SearchList = ArrayList<FirebaseResponse>()

    var productName : String = ""
    var productImage : String = ""
    var productPrice : String = ""
    var productStock : String = ""
    var productOrigin : String = ""
    var userID : String = ""



    private val productAPI = ProductApi()
    private val disposable = CompositeDisposable()

    fun refreshData(){
       getFromDataAPI()
    }

    fun updateProductList(newList: List<FirebaseResponse>) {
        products.value = newList
    }
    fun searchList(text: String) {
        SearchList.clear()
        for(search in productList){
            if(search.ProductName.lowercase().contains(text.lowercase()) == true){
                SearchList.add(search)
            }
        }

    }

    fun updateTRProductList(newList: List<FirebaseResponse>) {
        trProducts.value = newList
    }


    private fun getFromDataAPI(){
        productLoading.value = true
       disposable.add(
           productAPI.getAllData()
               .subscribeOn(Schedulers.newThread())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribeWith(object : DisposableSingleObserver<FirebaseAllProductsResponse>(){
                   override fun onSuccess(t: FirebaseAllProductsResponse) {
                       System.out.println("sadsad")
                       val sortedList = t.documents.sortedBy { it.fields.ProductOrigin.stringValue != "TR" }
                       System.out.println("sortedList" + sortedList)
                       System.out.println(arrayListOf(t.documents))
                       for(document in sortedList)  {
                           productName = document.fields.ProductName.stringValue
                           productPrice = document.fields.ProductPrice.stringValue
                           productStock = document.fields.ProductStock.integerValue
                           productImage = document.fields.ProductImage.stringValue
                           productOrigin = document.fields.ProductOrigin.stringValue
                           userID = document.fields.ProductID.stringValue
                           val newProduct = FirebaseResponse(userID,productImage, productPrice, productStock, productName,0)
                           productList.add(newProduct)
                           foreignProducttList.add(newProduct)

                           System.out.println("productList" + productList)

                       }
                       System.out.println(productList)
                       // products.value = productList
                       updateProductList(foreignProducttList)
                       foreignProducttList.clear()
                       productLoading.value = false
                       productError.value = false
                   }

                   override fun onError(e: Throwable) {
                       System.out.println("error")
                       productError.value = true
                       productLoading.value = false
                       e.printStackTrace()
                   }

               })
       )

        disposable.add(
            productAPI.getTurkishData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<FirebaseProductsResponse>(){
                    override fun onSuccess(t: FirebaseProductsResponse) {
                        System.out.println("sadsad")
                        System.out.println(arrayListOf(t.documents))
                        for(document in t.documents)  {
                            val productName = document.fields.ProductName.stringValue
                            val productPrice = document.fields.ProductPrice.stringValue
                            val productStock = document.fields.ProductStock.integerValue
                            val productImage = document.fields.ProductImage.stringValue
                            val userID = document.fields.ProductID.stringValue
                            val newProduct = FirebaseResponse(userID,productImage, productPrice, productStock, productName,0)
                            trProducttList.add(newProduct)
                            System.out.println("productList" + productList)

                        }
                        System.out.println(productList)
                        // products.value = productList
                        updateTRProductList(trProducttList)
                        trProducttList.clear()
                        productLoading.value = false
                        productError.value = false
                    }

                    override fun onError(e: Throwable) {
                        System.out.println("error")
                        productError.value = true
                        productLoading.value = false
                        e.printStackTrace()
                    }

                })
        )


    }


}
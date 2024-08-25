package com.io1.e_commerce.service

import com.io1.e_commerce.model.FirebaseAllProductsResponse
import com.io1.e_commerce.model.FirebaseProductsResponse
import com.io1.e_commerce.model.FirebaseResponse
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ProductApi {

    val BaseUrl = "https://firestore.googleapis.com/v1/projects/e-commerce-4c753/databases/(default)/documents/"


    val retrofit = Retrofit.Builder()
        .baseUrl(BaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(ForeignAPI::class.java)

     fun getData() : Single<FirebaseProductsResponse> {
        return retrofit.getForeignProducts()
    }

    fun getAllData() : Single<FirebaseAllProductsResponse> {
        return retrofit.getAllProducts()
    }

    fun getTurkishData() : Single<FirebaseProductsResponse> {
        return retrofit.getTurkishProducts()
    }
}
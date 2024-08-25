package com.io1.e_commerce.service

import com.io1.e_commerce.model.FirebaseAllProductsResponse
import com.io1.e_commerce.model.FirebaseProductsResponse
import com.io1.e_commerce.model.FirebaseResponse
import io.reactivex.Single
import retrofit2.http.GET

interface ForeignAPI {
    @GET("Foreign")
    fun getForeignProducts() : Single<FirebaseProductsResponse>

    @GET("Turkish")
    fun getTurkishProducts() : Single<FirebaseProductsResponse>

    @GET("Products")
    fun getAllProducts() : Single<FirebaseAllProductsResponse>
}


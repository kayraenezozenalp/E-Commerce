package com.io1.e_commerce.service

import com.io1.e_commerce.model.Fields
import com.io1.e_commerce.model.FirebaseProductsResponse
import com.io1.e_commerce.model.FirebaseResponse
import retrofit2.Response
import retrofit2.http.GET

interface foreign {
    @GET("Foreign")
    suspend fun service() : FirebaseProductsResponse
}

interface turkish {
    @GET("Turkish")
    suspend fun service() : FirebaseProductsResponse
}





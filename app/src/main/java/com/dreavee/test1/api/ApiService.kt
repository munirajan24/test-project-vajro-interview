package com.dreavee.test1.api

import com.dreavee.test1.model.ProductList
import retrofit2.http.GET

interface ApiService {


    @GET("5def7b172f000063008e0aa2")
    suspend fun getProductList(): ProductList

}
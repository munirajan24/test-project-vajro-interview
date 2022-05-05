package com.dreavee.test1.api

import com.dreavee.test1.model.ProductList

interface ApiHelper {


    suspend fun getProductList(): ProductList

}
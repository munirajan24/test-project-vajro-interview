package com.dreavee.test1.api

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {


    override suspend fun getProductList() = apiService.getProductList()

}
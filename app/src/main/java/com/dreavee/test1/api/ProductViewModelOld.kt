package com.dreavee.test1.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreavee.test1.api.ApiHelper
import com.dreavee.test1.model.ProductList
import com.dreavee.test1.room.DatabaseHelper
import com.dreavee.test1.room.entity.Product
import com.dreavee.test1.utils.Resource
import kotlinx.coroutines.launch

class ProductViewModelOld(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper
) : ViewModel() {

    //    private val users = MutableLiveData<Resource<List<ApiUser>>>()
    private val productList = MutableLiveData<Resource<ProductList>>()

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            productList.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper.getProductList()
                productList.postValue(Resource.success(usersFromApi))
            } catch (e: Exception) {
                productList.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    suspend fun addProductsToDB(product: Product) {
        dbHelper.insert(product)
    }

    fun getProducts(): LiveData<Resource<ProductList>> {
        return productList
    }

}
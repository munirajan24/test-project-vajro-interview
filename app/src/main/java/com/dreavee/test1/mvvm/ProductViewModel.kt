package com.dreavee.test1.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreavee.test1.model.ProductList
import com.dreavee.test1.room.entity.Product
import com.dreavee.test1.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProductViewModel(
    private val apiHelper: MainRepository
) : ViewModel() {

    //    private val users = MutableLiveData<Resource<List<ApiUser>>>()
     val productList = MutableLiveData<Resource<ProductList>>()

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
        apiHelper.insert(product)
    }

    suspend fun getCartProductQuantityById(product_id: Int?):Int? {
       return apiHelper.getCartProductQuantityById(product_id)
    }

    suspend fun getCartProductById(product_id: Int?):Product? {
       return apiHelper.getCartProductById(product_id)
    }

    suspend fun removeProductsFromDB(product: Product) {
        apiHelper.remove(product)
    }

    suspend fun updateProductsToDB(product: Product) {
        apiHelper.update(product)
    }

  suspend  fun getProductsFromCart(): Flow<List<Product>> {

        return apiHelper.getProductsFromDb()
    }

    fun getProducts(): LiveData<Resource<ProductList>> {
        return productList
    }

}
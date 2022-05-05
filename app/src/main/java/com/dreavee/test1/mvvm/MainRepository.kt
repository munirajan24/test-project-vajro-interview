package com.dreavee.test1.mvvm

import com.dreavee.test1.api.ApiService
import com.dreavee.test1.room.CartDatabase
import com.dreavee.test1.room.DatabaseHelper
import com.dreavee.test1.room.DatabaseHelperImpl
import com.dreavee.test1.room.entity.Product
import kotlinx.coroutines.flow.Flow

class MainRepository constructor(
    private val retrofitService: RetrofitService,
    private val appDatabase: CartDatabase
) {

    suspend fun getProductList() = retrofitService.getProductList()

    suspend fun getProductsFromDb(): Flow<List<Product>> =
        appDatabase.productDao().getCartProducts()

    suspend fun insert(product: Product) = appDatabase.productDao().insertProduct(product)

    suspend fun remove(product: Product) = appDatabase.productDao().removeProduct(product)

    suspend fun update(product: Product) = appDatabase.productDao().updateProduct(product)

    suspend fun getCartProductQuantityById(product_id: Int?): Int? =
        appDatabase.productDao().getCartProductQuantityById(product_id)

    suspend fun getCartProductById(product_id: Int?): Product? =
        appDatabase.productDao().getCartProductById(product_id)


}
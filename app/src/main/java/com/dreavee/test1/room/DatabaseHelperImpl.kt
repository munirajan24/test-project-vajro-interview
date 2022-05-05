package com.dreavee.test1.room

import com.dreavee.test1.room.entity.Product
import kotlinx.coroutines.flow.Flow

abstract class DatabaseHelperImpl(private val appDatabase: CartDatabase) : DatabaseHelper {

//    override fun getProducts(): Flow<List<Product>> = appDatabase.productDao().getCartProducts()

//    override fun insertAll(products: List<Product>) = appDatabase.productDao().insertAll(products)

//    override suspend fun insert(product: Product) = appDatabase.productDao().insertProduct(product)

}
package com.dreavee.test1.room.dao

import androidx.room.*
import com.dreavee.test1.room.entity.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(product: Product)

    @Delete
    suspend fun removeProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)

    @Query("SELECT * FROM product")
     fun getCartProducts(): Flow<List<Product>>

    @Query("SELECT product_quantity FROM product where product_id= :product_id")
    suspend fun getCartProductQuantityById(product_id: Int?): Int?


    @Query("SELECT * FROM product where product_id= :product_id")
    suspend fun getCartProductById(product_id: Int?): Product?


}
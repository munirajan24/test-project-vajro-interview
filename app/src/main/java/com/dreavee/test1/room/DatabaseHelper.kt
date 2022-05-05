package com.dreavee.test1.room

import com.dreavee.test1.room.entity.Product
import kotlinx.coroutines.flow.Flow

interface DatabaseHelper {

     fun getProducts(): Flow<List<Product>>

     fun insertAll(users: List<Product>)

     suspend fun insert(users: Product)

}
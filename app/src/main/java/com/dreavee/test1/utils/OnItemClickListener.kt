package com.dreavee.test1.utils

import com.dreavee.test1.model.Products
import com.dreavee.test1.room.entity.Product


interface OnItemClickListener {
    fun onAddClick(products: Products)
    fun onUpdate(products: Products)
}
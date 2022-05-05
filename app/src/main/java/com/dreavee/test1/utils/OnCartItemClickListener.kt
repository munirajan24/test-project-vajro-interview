package com.dreavee.test1.utils

import com.dreavee.test1.room.entity.Product


interface OnCartItemClickListener {
    fun onAddClick(products: Product)
    fun onUpdate(products: Product)
}
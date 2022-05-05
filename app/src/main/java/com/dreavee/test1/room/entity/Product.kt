package com.dreavee.test1.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "product_quantity") var productQuantity: Int? = 0,
    @ColumnInfo(name = "product_id") val product_id: Int?,
    @ColumnInfo(name = "sku") val sku: String?,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "thumb") val thumb: String?,
    @ColumnInfo(name = "zoom_thumb") val zoom_thumb: String?,
    @ColumnInfo(name = "options") val options: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "href") val href: String?,
    @ColumnInfo(name = "quantity") val quantity: Int?,
    @ColumnInfo(name = "images") val images: String?,
    @ColumnInfo(name = "price") val price: String?,
    @ColumnInfo(name = "special") val special: String?
)

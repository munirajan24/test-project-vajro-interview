package com.dreavee.test1.model

import com.google.gson.annotations.SerializedName

data class ProductList(

    @SerializedName("products") var products: List<Products>
)
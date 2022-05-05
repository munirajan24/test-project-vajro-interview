package com.dreavee.test1.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Products (

    @SerializedName("name") val name: String?,
    @SerializedName("id") val id: Int,
    @SerializedName("product_quantity") var productQuantity: Int? = 0,
    @SerializedName("product_id") val product_id: Int?,
    @SerializedName("sku") val sku: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("thumb") val thumb: String?,
    @SerializedName("zoom_thumb") val zoom_thumb: String?,
    @SerializedName("options") val options: List<String>,
    @SerializedName("description") val description: String?,
    @SerializedName("href") val href: String?,
    @SerializedName("quantity") var quantity: Int?,
    @SerializedName("images") val images: List<String?>,
    @SerializedName("price") val price: String?,
    @SerializedName("special") val special: String?
): Parcelable
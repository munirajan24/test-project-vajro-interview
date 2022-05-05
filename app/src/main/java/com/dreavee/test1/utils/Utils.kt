package com.dreavee.test1.utils

import com.dreavee.test1.room.entity.Product
import com.dreavee.test1.model.Products


class Utils {
    companion object {
        fun apiToDbProduct(apiProduct: Products): Product {
            var product = Product(
                apiProduct.id,
                apiProduct.name,
                apiProduct.productQuantity,
                apiProduct.product_id,
                apiProduct.sku,
                apiProduct.image,
                apiProduct.thumb,
                apiProduct.zoom_thumb,
                apiProduct.options.joinToString(),
                apiProduct.description,
                apiProduct.href,
                apiProduct.quantity,
                apiProduct.images.joinToString(),
                apiProduct.price,
                apiProduct.special
            )
            return product
        }

        fun dbToApiProduct(apiProduct: Product): Products {
            var product = Products(
                apiProduct.name,
                apiProduct.id,
                apiProduct.productQuantity,
                apiProduct.product_id,
                apiProduct.sku,
                apiProduct.image,
                apiProduct.thumb,
                apiProduct.zoom_thumb,
                emptyList(),
                apiProduct.description,
                apiProduct.href,
                apiProduct.quantity,
                emptyList(),
                apiProduct.price,
                apiProduct.special
            )
            return product
        }

        fun stringToWords(s : String) = s.trim().splitToSequence(' ')
            .filter { it.isNotEmpty() } // or: .filter { it.isNotBlank() }
            .toList()
    }

}
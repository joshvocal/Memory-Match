package com.example.memorymatcher.data.network.response

import com.example.memorymatcher.data.db.entity.Product


data class ShopifyProductsResponse(

    /**
     * Response from the payload
     */
    val products: List<Product>
)
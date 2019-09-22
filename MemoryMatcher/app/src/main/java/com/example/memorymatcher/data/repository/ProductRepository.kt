package com.example.memorymatcher.data.repository

import android.util.Log
import com.example.memorymatcher.data.db.ProductDatabase
import com.example.memorymatcher.data.db.entity.Product
import com.example.memorymatcher.data.network.response.ShopifyProductsResponse
import com.example.memorymatcher.data.network.service.ShopifyProductsService
import com.example.memorymatcher.util.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException


class ProductRepository(
    private val shopifyProductsService: ShopifyProductsService,
    private val productDatabase: ProductDatabase
) {

    suspend fun getProductsFromDatabase(): List<Product> {
        return withContext(Dispatchers.IO) {
            // If we don't have data in our database, use internet for the first time
            val productList = productDatabase.getProductDao().getProductList()

            if (productList.isNotEmpty()) {
                productList.shuffled()
            } else {
                getProductsFromNetwork()
            }
        }
    }

    private fun saveProductsToDatabase(response: ShopifyProductsResponse) {
        Coroutines.io {
            productDatabase.getProductDao().insertProduct(response.products)
        }
    }

    suspend fun getProductsFromNetwork(): List<Product> {
        try {
            val fetchedShopifyProducts = shopifyProductsService
                .getCurrentProductsAsync(3)
                .await()

            saveProductsToDatabase(fetchedShopifyProducts)

            return fetchedShopifyProducts.products
        } catch (e: IOException) {
            Log.e("Connectivity", "No internet connection", e)

        }

        return emptyList()
    }
}
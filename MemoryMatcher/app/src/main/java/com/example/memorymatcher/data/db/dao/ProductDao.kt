package com.example.memorymatcher.data.db.dao

import androidx.room.*
import com.example.memorymatcher.data.db.entity.Product

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(products: List<Product>)

    @Update
    fun updateProduct(product: Product)

    @Query("SELECT * FROM Product WHERE id = :id_")
    fun getProduct(id_: Int): Product

    @Query("SELECT * FROM Product")
    fun getProductList(): List<Product>
}
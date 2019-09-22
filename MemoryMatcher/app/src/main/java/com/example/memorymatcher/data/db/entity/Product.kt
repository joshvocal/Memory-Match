package com.example.memorymatcher.data.db.entity


import androidx.room.Embedded
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = [("id")])
data class Product(
    val id: Long,

    val title: String,

    @Embedded(prefix = "image_")
    val image: Image
)

data class Image(
    val id: Long,

    @SerializedName("product_id")
    val productId: Long,

    val width: Int,

    val height: Int,

    val src: String
)
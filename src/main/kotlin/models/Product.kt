package models

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String,
    val shortDescription: String,
    val editorDescription: String,
    val category: String,
    val brand: String,
    val sku: String,
    val variant: ProductVariant,
    val unitPrice: Double,
    val priceCurrency: String,
    val inStock: Boolean,
    val imageUrl: String?
)

@Serializable
data class ProductVariant(val model: String, val color: String, val materials: String, val size: String, val gender: String)

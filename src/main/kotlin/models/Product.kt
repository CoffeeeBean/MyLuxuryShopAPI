package models

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class Product(
    val id: String,

    @JsonProperty("shortDescription")
    val shortDescription: String?,
    @JsonProperty("editorDescription")
    val editorDescription: String?,
    val category: String?,
    val brand: String?,
    val sku: String?,
    val variant: ProductVariant?,
    @JsonProperty("unitPrice")
    val unitPrice: Double?,
    @JsonProperty("priceCurrency")
    val priceCurrency: String?,
    @JsonProperty("inStock")
    val inStock: Boolean?
)

@Serializable
data class ProductVariant(val model: String, val color: String, val materials: List<Material>, val size: String, val gender: String)

@Serializable
data class Material(val description: String)

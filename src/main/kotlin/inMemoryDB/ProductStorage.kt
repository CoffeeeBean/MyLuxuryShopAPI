package inMemoryDB

import models.*

val productStorage = mutableListOf<Product>(
    Product(
        id="100",
        shortDescription="Le Petit Bambino suede tote" ,
        editorDescription="Le Petit Bambino suede tote cross body bag.",
        category = "Cross Body Bag",
        brand = "Jacquemus",
        sku="SKU0000001",
        variant= ProductVariant(model = "Cross Body Bag",color = "Blue",materials = listOf(Material(description = "Blue suede"), Material("Leather")),size = "U",gender = "Woman"),
        unitPrice=350.50 ,
        priceCurrency="EUR",
        inStock = true)
)
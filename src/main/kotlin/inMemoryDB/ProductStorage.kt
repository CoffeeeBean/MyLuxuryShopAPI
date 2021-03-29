package inMemoryDB

import models.*

val productStorage = mutableListOf(
    Product(
        id = "200",
        shortDescription = "Le Petit Bambino suede tote",
        editorDescription = "Le Petit Bambino suede tote cross body bag.",
        category = "Cross Body Bag",
        brand = "Jacquemus",
        sku = "SKU0000001",
        variant = ProductVariant(model = "Cross Body Bag", color = "Blue", materials = listOf(Material(description = "Blue suede"), Material("Leather")), size = "U", gender = "Woman"),
        unitPrice = 350.50,
        priceCurrency = "EUR",
        inStock = true,
        imageUrl = "https://www.net-a-porter.com/content/images/cms/ycm/resource/blob/480508/676dddec97d046278c9518c3c83340b9/image-2-desktop-data.jpg/w1500_q80.jpg"
    )
)

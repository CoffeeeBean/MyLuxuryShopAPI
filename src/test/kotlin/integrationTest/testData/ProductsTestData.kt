package integrationTest.testData
import models.Product
import models.ProductVariant

class ProductsTestData {

    companion object {
        fun getProductTestData(): Map<String, Product> {
            return products
        }

        private val products = mapOf<String, Product>(
            "100" to Product(
                id = "100",
                shortDescription = "Le Petit Bambino suede tote",
                editorDescription = "Le Petit Bambino suede tote cross body bag.",
                category = "Cross Body Bag",
                brand = "Jacquemus",
                sku = "SKU0000001",
                variant = ProductVariant(model = "Cross Body Bag", color = "Blue", materials = "Blue suede", size = "U", gender = "Woman"),
                unitPrice = 350.50,
                priceCurrency = "EUR",
                inStock = true,
                imageUrl = "https://images/someimage.jpg"
            ),
            "200" to Product(
                id = "200",
                shortDescription = "Mair 90 leather pumps",
                editorDescription = "Mair 90 leather pumps",
                category = "Shoes",
                brand = "Jimmy Choo",
                sku = "SKU99999",
                variant = ProductVariant(
                    model = "High Heel",
                    color = "Blue",
                    materials = "White leather",
                    size = "7",
                    gender = "Woman"
                ),
                unitPrice = 350.50,
                priceCurrency = "EUR",
                inStock = true,
                imageUrl = "https://images/someimage.jpg"
            ),
            "300" to Product(
                id = "300",
                shortDescription = "Mair 90 leather pumps",
                editorDescription = "Mair 90 leather pumps",
                category = "Shoes",
                brand = "Jimmy Choo",
                sku = "SKU99999",
                variant = ProductVariant(
                    model = "High Heel",
                    color = "Blue",
                    materials = "White leather",
                    size = "8",
                    gender = "Woman"
                ),
                unitPrice = 350.50,
                priceCurrency = "EUR",
                inStock = true,
                imageUrl = "https://images/someimage.jpg"
            ),
            "400" to Product(
                id = "400",
                shortDescription = "Valentino Garavani 02 Bow Edition Atelier small textured-leather tote",
                editorDescription = "Valentino Garavani 02 Bow Edition Atelier small textured-leather tote",
                category = "Bag",
                brand = "Valentino",
                sku = "SKU99999",
                variant = ProductVariant(
                    model = "Toe Bag",
                    color = "Blue",
                    materials = "White leather",
                    size = "8",
                    gender = "Woman"
                ),
                unitPrice = 900.00,
                priceCurrency = "EUR",
                inStock = true,
                imageUrl = "https://images/someimage.jpg"
            )
        )
    }
}

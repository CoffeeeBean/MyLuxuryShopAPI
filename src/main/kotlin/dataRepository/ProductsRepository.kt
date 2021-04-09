package dataRepository

import com.jillesvangurp.eskotlinwrapper.IndexRepository
import models.Product

class ProductsRepository (
    //injected via DI eventually
    private val productsIndex: IndexRepository<Product> = ProductsIndex().initialise()
) {
    fun getProduct(id: String): Product? = productsIndex.get(id)
}
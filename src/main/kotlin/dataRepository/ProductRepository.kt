package dataRepository

import com.jetbrains.handson.httpapi.esClient
import com.jillesvangurp.eskotlinwrapper.SearchResults
import com.jillesvangurp.eskotlinwrapper.dsl.match
import com.jillesvangurp.eskotlinwrapper.dsl.matchAll
import models.Product
import org.elasticsearch.action.search.configure
import org.elasticsearch.client.indexRepository

class ProductRepository {

    fun getAllProducts(): SearchResults<Product> {
        val productRepo = esClient.indexRepository<Product>("products", refreshAllowed = true)

        val results = productRepo.search {
            configure {
                query = matchAll()
            }
        }
        return results
    }

    fun getSingleProducts(id: String): SearchResults<Product> {
        val productRepo = esClient.indexRepository<Product>("products")
        val results = productRepo.search {
            configure {
                query = match("id", id)
            }
        }
        return results
    }

    fun insertProduct(product: Product) {
        val productRepo = esClient.indexRepository<Product>("products", refreshAllowed = true)
        productRepo.index(product.id, product)
    }
}

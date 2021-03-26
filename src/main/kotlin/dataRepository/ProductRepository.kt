package dataRepository

import com.jetbrains.handson.httpapi.esClient
import com.jillesvangurp.eskotlinwrapper.*
import com.jillesvangurp.eskotlinwrapper.dsl.match
import com.jillesvangurp.eskotlinwrapper.dsl.matchAll
import models.Product
import org.elasticsearch.action.search.configure
import org.elasticsearch.action.search.source
import org.elasticsearch.client.Request
import org.elasticsearch.client.indexRepository

class ProductRepository {

    fun getProducts(jsonQuery : String? = ""): SearchResults<Product> {
        val productRepo = ProductIndexRepository.getProductIndexRepositoryInstance()
        if(jsonQuery != ""){
            return productRepo.search {
                source(jsonQuery!!)
            }
        }
        return productRepo.search {
            configure {
                query = matchAll()
            }
        }
    }

    fun getSingleProducts(id: String): SearchResults<Product> {
        val productRepo = ProductIndexRepository.getProductIndexRepositoryInstance()
        return productRepo.search {
            configure {
                query = match("id", id)
            }
        }
    }

    fun insertProduct(product: Product) {
        val productRepo = ProductIndexRepository.getProductIndexRepositoryInstance()
        productRepo.index(product.id, product)
    }
}



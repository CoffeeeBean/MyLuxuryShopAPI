package dataRepository

import com.jillesvangurp.eskotlinwrapper.*
import com.jillesvangurp.eskotlinwrapper.dsl.match
import com.jillesvangurp.eskotlinwrapper.dsl.matchAll
import models.Product
import org.elasticsearch.action.search.configure
import org.elasticsearch.action.search.source

class ProductRepository {

    fun getProducts(jsonQuery: String? = ""): SearchResults<Product> {
        val productRepo = ProductIndexRepository.getProductIndexRepositoryInstance()
        if (jsonQuery != "") {
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

    fun insertProduct(product: Product): String {
        val productRepo = ProductIndexRepository.getProductIndexRepositoryInstance()
        productRepo.index(product.id, product)
        val newProduct = productRepo.get(product.id)
        return newProduct!!.id
    }
}

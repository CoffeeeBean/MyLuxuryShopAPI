package dataRepository

import com.fasterxml.jackson.databind.ObjectMapper
import com.jillesvangurp.eskotlinwrapper.*
import com.jillesvangurp.eskotlinwrapper.dsl.match
import com.jillesvangurp.eskotlinwrapper.dsl.matchAll
import models.Product
import org.elasticsearch.action.search.configure
import org.elasticsearch.action.search.source
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.indexRepository

class ProductRepository(elasticRestHighLevelClient: RestHighLevelClient) {

    private val elasticRestHighLevelClient= elasticRestHighLevelClient
    private val productIndexRepository= ProductIndexRepository()

    private inner class ProductIndexRepository{
            fun getProductIndexRepositoryInstance(): IndexRepository<Product> {
                val modelReaderAndWriter = CustomModelReaderAndWriter(
                    Product::class,
                    ObjectMapper().findAndRegisterModules()
                )
                return this@ProductRepository.elasticRestHighLevelClient.indexRepository("products", modelReaderAndWriter = modelReaderAndWriter)
            }
    }

    fun getProducts(jsonQuery: String? = ""): SearchResults<Product> {
        val productRepo = productIndexRepository.getProductIndexRepositoryInstance()
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
        val productRepo = productIndexRepository.getProductIndexRepositoryInstance()
        return productRepo.search {
            configure {
                query = match("id", id)
            }
        }
    }

    fun insertProduct(product: Product): String {
        val productRepo = productIndexRepository.getProductIndexRepositoryInstance()
        productRepo.index(product.id, product)
        val newProduct = productRepo.get(product.id)
        return newProduct!!.id
    }
}

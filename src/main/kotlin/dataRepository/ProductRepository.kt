package dataRepository

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.jetbrains.handson.httpapi.esClient
import com.jillesvangurp.eskotlinwrapper.JacksonModelReaderAndWriter
import com.jillesvangurp.eskotlinwrapper.ModelReaderAndWriter
import com.jillesvangurp.eskotlinwrapper.SearchResults
import com.jillesvangurp.eskotlinwrapper.dsl.match
import com.jillesvangurp.eskotlinwrapper.dsl.matchAll
import models.Product
import org.elasticsearch.action.search.configure
import org.elasticsearch.client.indexRepository

class ProductRepository {

    fun getAllProducts(): SearchResults<Product> {
        val objectMapper = ObjectMapper()
        // enable Kotlin integration and whatever else is on the classpath
        objectMapper.findAndRegisterModules()
        // make sure we convert names with underscores properly to and
        // from kotlin (camelCase)
        objectMapper.propertyNamingStrategy = PropertyNamingStrategies.LOWER_CAMEL_CASE
        val productRepo = esClient.indexRepository<Product>("products", refreshAllowed = true)
        esClient.use {
            val customSerde = JacksonModelReaderAndWriter(Product::class, objectMapper)
        }

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



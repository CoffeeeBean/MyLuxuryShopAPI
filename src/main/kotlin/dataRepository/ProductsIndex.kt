package dataRepository

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jillesvangurp.eskotlinwrapper.IndexRepository
import com.jillesvangurp.eskotlinwrapper.JacksonModelReaderAndWriter
import models.Product
import org.elasticsearch.client.indexRepository

class ProductsIndex {
    private val modelReaderAndWriter: ObjectMapper = jacksonObjectMapper()
        .setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)

    fun initialise(): IndexRepository<Product> {
        return EsClientBuilder.restHighLevelClient.indexRepository(
            index = "products",
            modelReaderAndWriter = JacksonModelReaderAndWriter(Product::class, modelReaderAndWriter),
            refreshAllowed = true // only in dev
        )
    }
}
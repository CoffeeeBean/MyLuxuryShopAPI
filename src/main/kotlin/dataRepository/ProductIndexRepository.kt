package dataRepository

import com.fasterxml.jackson.databind.ObjectMapper
import com.jetbrains.handson.httpapi.esClient
import com.jillesvangurp.eskotlinwrapper.CustomModelReaderAndWriter
import com.jillesvangurp.eskotlinwrapper.IndexRepository
import models.Product
import org.elasticsearch.client.indexRepository

object ProductIndexRepository {

    fun getProductIndexRepositoryInstance(): IndexRepository<Product> {
        val modelReaderAndWriter = CustomModelReaderAndWriter(
            Product::class,
            ObjectMapper().findAndRegisterModules()
        )
        return esClient.indexRepository("products", modelReaderAndWriter = modelReaderAndWriter)
    }
}

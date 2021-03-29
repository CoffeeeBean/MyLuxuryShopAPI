package dataRepository

import com.fasterxml.jackson.databind.ObjectMapper
import com.jillesvangurp.eskotlinwrapper.CustomModelReaderAndWriter
import com.jillesvangurp.eskotlinwrapper.IndexRepository
import models.Product
import org.elasticsearch.client.indexRepository

@Deprecated("use inner class instead")
object ProductIndexRepository {

    fun getProductIndexRepositoryInstance(): IndexRepository<Product> {
        val modelReaderAndWriter = CustomModelReaderAndWriter(
            Product::class,
            ObjectMapper().findAndRegisterModules()
        )

        return EsClientBuilder.restHighLevelClient.indexRepository("products", modelReaderAndWriter = modelReaderAndWriter)
    }
}

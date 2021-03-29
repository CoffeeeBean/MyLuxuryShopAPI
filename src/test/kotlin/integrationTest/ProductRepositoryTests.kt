package integrationTest

import com.fasterxml.jackson.databind.ObjectMapper
import com.jillesvangurp.eskotlinwrapper.CustomModelReaderAndWriter
import dataRepository.EsClientBuilder
import dataRepository.ProductIndexRepository
import dataRepository.ProductRepository
import inMemoryDB.productStorage
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import models.Material
import models.Product
import models.ProductVariant
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.indexRepository
import org.testcontainers.elasticsearch.ElasticsearchContainer

class ProductRepositoryTests : FunSpec({

    var esContainer: ElasticsearchContainer = ElasticsearchContainer()
    beforeSpec{
        esContainer = ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.9.3")
        esContainer.start()
        val testEsClient = RestHighLevelClient(RestClient.builder(HttpHost.create(esContainer.httpHostAddress)))

        val modelReaderAndWriter = CustomModelReaderAndWriter(
            Product::class,
            ObjectMapper().findAndRegisterModules()
        )
        val productRepo = testEsClient.indexRepository("products", modelReaderAndWriter = modelReaderAndWriter)
        productRepo.index(id = "200", productStorage[0])

    }


    test("GetSingleProduct should return one Product when the product exist") {
        val productRepository = ProductRepository(RestHighLevelClient(RestClient.builder(HttpHost.create(esContainer.httpHostAddress))))
        val productSearchResult = productRepository.getSingleProducts("200")
        productSearchResult.hits.count() shouldBe 1
        val product = productSearchResult.mappedHits.first()
        product.id shouldBe "200"
    }
    test("GetProduct with empty json query should return all product") {
        val productRepository = ProductRepository(EsClientBuilder.restHighLevelClient)
        val productSearchResult = productRepository.getProducts()
        productSearchResult.hits.count() shouldBe 3
    }
    test("GetProduct with not empty json query should return filtered product") {
        val productRepository = ProductRepository(EsClientBuilder.restHighLevelClient)
        val productSearchResult = productRepository.getProducts(
            "{\n" +
                "   \"query\": {\n" +
                "     \"bool\" : {\n" +
                "       \"filter\": [\n" +
                "         {\"match\":{\"variant.model\":\"Toe Bag\"}},\n" +
                "         {\"match\":{\"brand\":\"valentino\"}}\n" +
                "       ]\n" +
                "     }\n" +
                "   }\n" +
                " }"
        )
        productSearchResult.hits.count() shouldBe 1
        val product = productSearchResult.mappedHits.first()
        product.variant.model shouldBe "Toe Bag"
        product.brand shouldBe "Valentino"
    }
    test("insert product should insert a new product and return the new product id") {
        val productRepository = ProductRepository(EsClientBuilder.restHighLevelClient)
        val newProduct = Product(
            id = "newProductId",
            shortDescription = "Le Petit Bambino suede tote",
            editorDescription = "Le Petit Bambino suede tote cross body bag.",
            category = "Cross Body Bag",
            brand = "Jacquemus",
            sku = "SKU0000001",
            variant = ProductVariant(model = "Cross Body Bag", color = "Blue", materials = listOf(Material(description = "Blue suede"), Material("Leather")), size = "U", gender = "Woman"),
            unitPrice = 350.50,
            priceCurrency = "EUR",
            inStock = true,
            imageUrl = "https://image/image.jpg"
        )
        val newProductId = productRepository.insertProduct(newProduct)
        newProductId shouldBe "newProductId"

        // clean test data
        val productRepo = ProductIndexRepository.getProductIndexRepositoryInstance()
        productRepo.delete(newProductId)
    }
}
)

package integrationTest

import com.fasterxml.jackson.databind.ObjectMapper
import com.jillesvangurp.eskotlinwrapper.CustomModelReaderAndWriter
import dataRepository.ProductRepository
import integrationTest.testData.ProductsTestData
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import models.Product
import models.ProductVariant
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.indexRepository
import org.testcontainers.elasticsearch.ElasticsearchContainer

class ProductRepositoryTests : FunSpec({

    var testEsRestHighLevelClient : RestHighLevelClient? = null
    beforeSpec {
        val esContainer = ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.9.3")
        esContainer.start()
        testEsRestHighLevelClient = RestHighLevelClient(RestClient.builder(HttpHost.create(esContainer.httpHostAddress)))

        val modelReaderAndWriter = CustomModelReaderAndWriter(
            Product::class,
            ObjectMapper().findAndRegisterModules()
        )
        val productIndexRepository = testEsRestHighLevelClient!!.indexRepository("products", modelReaderAndWriter = modelReaderAndWriter)

        productIndexRepository.bulk {
            ProductsTestData.getProductTestData().entries.forEach() {
                index(it.key, it.value)
            }
        }
    }

    test("GetSingleProduct should return one Product when the product exist") {
        val productRepository = ProductRepository(testEsRestHighLevelClient!!)
        val productSearchResult = productRepository.getSingleProducts("200")
        productSearchResult.hits.count() shouldBe 1
        val product = productSearchResult.mappedHits.first()
        product.id shouldBe "200"
    }
    test("GetProduct with empty json query should return all product") {
        val productRepository = ProductRepository(testEsRestHighLevelClient!!)
        val productSearchResult = productRepository.getProducts()
        productSearchResult.hits.count() shouldBe 4
    }
    test("GetProduct with not empty json query should return filtered product") {
        val productRepository = ProductRepository(testEsRestHighLevelClient!!)
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
        val productRepository = ProductRepository(testEsRestHighLevelClient!!)
        val newProduct = Product(
            id = "newProductId",
            shortDescription = "Le Petit Bambino suede tote",
            editorDescription = "Le Petit Bambino suede tote cross body bag.",
            category = "Cross Body Bag",
            brand = "Jacquemus",
            sku = "SKU0000001",
            variant = ProductVariant(model = "Cross Body Bag", color = "Blue", materials = "Blue suede", size = "U", gender = "Woman"),
            unitPrice = 350.50,
            priceCurrency = "EUR",
            inStock = true,
            imageUrl = "https://image/image.jpg"
        )
        val newProductId = productRepository.insertProduct(newProduct)
        newProductId shouldBe "newProductId"

    }
}
)

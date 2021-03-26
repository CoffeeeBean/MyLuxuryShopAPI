import dataRepository.ProductRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ProductRepositoryTest : FunSpec({
    test("GetSingleProduct should return one Product when the product exist"){
        val productRepository = ProductRepository()
        val productSearchResult = productRepository.getSingleProducts("200")
        productSearchResult.hits.count() shouldBe 1
        val product = productSearchResult.mappedHits.first()
        product.id shouldBe "200"
    }
    test("GetProduct with empty json query should return all product"){
        val productRepository = ProductRepository()
        val productSearchResult = productRepository.getProducts()
        productSearchResult.hits.count() shouldBe 3
    }
    test("GetProduct with not empty json query should return filtered product"){
        val productRepository = ProductRepository()
        val productSearchResult = productRepository.getProducts("{\n" +
                "   \"query\": {\n" +
                "     \"bool\" : {\n" +
                "       \"filter\": [\n" +
                "         {\"match\":{\"variant.model\":\"Toe Bag\"}},\n" +
                "         {\"match\":{\"brand\":\"valentino\"}}\n" +
                "       ]\n" +
                "     }\n" +
                "   }\n" +
                " }")
        productSearchResult.hits.count() shouldBe 1
        val product = productSearchResult.mappedHits.first()
        product.variant.model shouldBe "Toe Bag"
        product.brand shouldBe "Valentino"
    }
}
)
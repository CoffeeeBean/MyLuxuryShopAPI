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
})
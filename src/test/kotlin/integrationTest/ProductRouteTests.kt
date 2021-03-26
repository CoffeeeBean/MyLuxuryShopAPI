package integrationTest

import com.jetbrains.handson.httpapi.module
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.*
import io.ktor.server.testing.*

/*
still using the real ES instance!!!
bypass only the netty part
 */
class ProductRouteTests : FunSpec({
    test("getSingleProductRoute should return one product with correct JSON format") {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/product/200").apply {

                val expectedResponse = """{
          "id" : "200",
          "shortDescription" : "Oversized double-breasted grain de poudre blazer",
          "editorDescription" : "Oversized double-breasted grain de poudre blazer",
          "category" : "Blazer",
          "brand" : "Bottega Veneta",
          "sku" : "SKU0000002",
          "variant" : {
            "model" : "Oversize Blazer",
            "color" : "Brown",
            "materials" : [
              {
                "description" : "Cotton"
              }
            ],
            "size" : "L",
            "gender" : "Woman"
          },
          "unitPrice" : 1500,
          "priceCurrency" : "EUR",
          "inStock" : true,
          "imageUrl" : "https://www.net-a-porter.com/content/images/cms/ycm/resource/blob/480508/676dddec97d046278c9518c3c83340b9/image-2-desktop-data.jpg/w1500_q80.jpg"
        }"""
                response.content!!.shouldEqualJson(expectedResponse)
                response.status() shouldBe HttpStatusCode.OK
            }
        }
    }
})

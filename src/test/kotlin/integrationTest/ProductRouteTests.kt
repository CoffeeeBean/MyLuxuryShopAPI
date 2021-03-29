package integrationTest

import com.jetbrains.handson.httpapi.module
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import models.Product

/*
still using the real ES instance!!!
bypass only the netty part
Ref https://ktor.io/docs/testing.html
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
    test("GetProducts given a json filter should return a list of products") {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "product/query") {
                // addHeader("Accept", "text/plain")
                addHeader("Content-Type", "application/json")
                setBody(
                    """{
                   "query": {
                     "bool" : {
                       "filter": [
                         {"match":{"brand":"BURBERRY"}}
                       ]
                     }
                   }
                 }"""
                )
            }.apply {

                val stringListSerializer: KSerializer<List<Product>> = ListSerializer(Product.serializer())
                val products =
                    Json.decodeFromString<List<Product>>(stringListSerializer, response.content!!)
                for (product in products) {
                    product.brand shouldBe "BURBERRY"
                }
                response.status() shouldBe HttpStatusCode.OK
            }
        }
    }
    test("AddProduct should return confirmation message") {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "product/add") {
                addHeader("Content-Type", "application/json")
                setBody(
                    """{
                        "id": "99999",
                        "shortDescription": "Leather-trimmed printed coated-canvas tote",
                        "editorDescription": "Elegant and timeless in its simplicity, Burberry's tote is a worthy wardrobe investment. Trimmed with leather, it's made from coated-canvas in a generous hold-all size and patterned with the 'TB' monogram originally created by Riccardo Tisci and Peter Saville.",
                        "category": "Bag",
                        "brand": "BURBERRY",
                        "sku": "SKU000555",
                        "variant": {
                            "model": "Toe Bag",
                            "color": "Brown",
                            "materials": [
                                {
                                    "description": "Tan textured-leather "
                                }
                            ],
                            "size": "U",
                            "gender": "Woman"
                        },
                        "unitPrice": 980,
                        "priceCurrency": "EUR",
                        "inStock": true,
                        "imageUrl": "https://cache.net-a-porter.com/images/products/1248519/1248519_in_920_q80.jpg"
                    }"""
                )
            }.apply {
                response.status() shouldBe HttpStatusCode.OK
                response.content!! shouldBe "New product added id=99999"
            }
        }
    }

})

package routes

import dataRepository.EsClientBuilder
import dataRepository.ProductRepository
import dataRepository.ProductsRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import models.Product

fun Route.getHealthStatus(){
    get("/health") {
        call.respondText("The API is Healthy!")
    }
}

fun Route.getProduct() {
    get("/products/{id}") {
        val productsRepository = ProductsRepository()
        try {
            val id = call.parameters["id"]!!
            val product = productsRepository.getProduct(id)
            if (product != null)
                call.respond(HttpStatusCode.OK, product)
            else
                call.respond(HttpStatusCode.NotFound)
        } catch (ex: Exception) {
            println("Exception: ${ex.message}") //add logger here
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}

fun Route.getSingleProductRoute() {
    get("/product/{id}") {
        val id = call.parameters["id"] ?: return@get call.respondText("Bad Request", status = HttpStatusCode.BadRequest)
        try{
            val productRepository = ProductRepository(EsClientBuilder.restHighLevelClient)
            val productSearchResult = productRepository.getSingleProducts(id)
            if (productSearchResult.hits.count() == 0) {
                return@get call.respondText(
                    "Not Found",
                    status = HttpStatusCode.NotFound
                )
            }
            val product = productSearchResult.mappedHits.first()
            call.respond(product)
        }catch (e: Exception){
            return@get call.respondText(e.stackTraceToString(), status = HttpStatusCode.InternalServerError)
        }

    }
}

fun Route.getProducts() {
    post("product/query") {
        val params = call.receive<String>()
        val productRepository = ProductRepository(EsClientBuilder.restHighLevelClient)
        val allProducts = productRepository.getProducts(params)
        val productToReturn = mutableListOf<Product>()

        allProducts.mappedHits.forEach { productToReturn.add(it) }
        call.respond(productToReturn)
    }
}

fun Route.addProduct() {
    post("/product/add") {
        val product = call.receive<Product>()
        val productRepository = ProductRepository(EsClientBuilder.restHighLevelClient)
        val newProductId = productRepository.insertProduct(product)
        call.respondText("New product added id=$newProductId", status = HttpStatusCode.OK)
    }
}

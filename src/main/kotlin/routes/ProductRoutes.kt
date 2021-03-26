package routes

import dataRepository.ProductRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import models.Product

fun Route.getSingleProductRoute() {
    get("/product/{id}") {
        val id = call.parameters["id"] ?: return@get call.respondText("Bad Request", status = HttpStatusCode.BadRequest)
        val productRepository = ProductRepository()
        val productSearchResult = productRepository.getSingleProducts(id)
        if (productSearchResult.hits.count() == 0) {
            return@get call.respondText(
                "Not Found",
                status = HttpStatusCode.NotFound
            )
        }
        val product = productSearchResult.mappedHits.first()
        call.respond(product)
    }
}

fun Route.getProducts() {
    get("product/query/") {
        val productRepository = ProductRepository()
        val allProducts = productRepository.getProducts()
        val productToReturn = mutableListOf<Product>()

        allProducts.mappedHits.forEach { productToReturn.add(it) }
        call.respond(productToReturn)
    }
}

fun Route.addProduct() {
    post("/product/add") {
        val product = call.receive<Product>()
        val productRepository = ProductRepository()
        productRepository.insertProduct(product)
        call.respondText("New product added", status = HttpStatusCode.Accepted)
    }
}

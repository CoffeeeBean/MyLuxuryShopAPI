package routes

import dataRepository.ProductRepository
import inMemoryDB.productStorage
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import models.Product

fun Route.getSingleProductRoute() {
    get("/product/{id}") {
        val id = call.parameters["id"] ?: return@get call.respondText("Bad Request", status = HttpStatusCode.BadRequest)
        val order = productStorage.find { it.id == id } ?: return@get call.respondText(
            "Not Found",
            status = HttpStatusCode.NotFound
        )
        call.respond(order)
    }
}

fun Route.getProducts() {
    get("product/query/") {
        val productRepository= ProductRepository()
        val allProducts = productRepository.getAllProducts()
        val productToReturn = mutableListOf<Product>()

        allProducts.mappedHits.forEach { productToReturn.add(it) }
        call.respond( productToReturn )
    }
}
fun Route.addProduct() {
    post("/product/add") {
        val product = call.receive<Product>()
        val productRepository=ProductRepository()
        productRepository.insertProduct(product)
        call.respondText("New product added", status = HttpStatusCode.Accepted)
    }
}

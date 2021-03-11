package routes

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

fun Route.addProduct(){
    post("/product/add") {
        val product = call.receive<Product>()
        productStorage.add(product)
        call.respondText("New product added", status = HttpStatusCode.Accepted)
    }
}

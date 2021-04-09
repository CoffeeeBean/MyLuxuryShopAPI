package com.jetbrains.handson.httpapi

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import routes.*
import io.ktor.jackson.jackson

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    try {
        install(ContentNegotiation) {
            jackson {}
        }
        install(CORS) {
            header("Access-Control-Allow-Origin")
            allowNonSimpleContentTypes = true
            anyHost()
        }
        registerProductRoutes()
    }catch (e: Exception){
        System.console().printf(e.stackTraceToString())
    }

}

fun Application.registerProductRoutes() {
    routing {
        getHealthStatus()
        getSingleProductRoute()
        getProduct()
        addProduct()
        getProducts()
    }
}

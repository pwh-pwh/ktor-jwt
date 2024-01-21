package com.coderpwh.routing

import com.coderpwh.routing.request.LoginRequest
import com.coderpwh.service.JwtService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoute(jwtService: JwtService) {
    post {
        val loginRequest = call.receive<LoginRequest>()
        jwtService.createToken(loginRequest)?.let {
            call.respond(mapOf("token" to it))
        } ?: return@post call.respond(HttpStatusCode.Unauthorized)
    }
}
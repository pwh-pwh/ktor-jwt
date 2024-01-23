package com.coderpwh.routing

import com.coderpwh.routing.request.LoginRequest
import com.coderpwh.routing.request.RefreshTokenRequest
import com.coderpwh.routing.response.RefreshTokenResponse
import com.coderpwh.service.JwtService
import com.coderpwh.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoute(userService: UserService) {
    post {
        val loginRequest = call.receive<LoginRequest>()
        userService.authenticate(loginRequest)?.let {
            call.respond(it)
        } ?: return@post call.respond(HttpStatusCode.Unauthorized)
    }

    post("/refresh") {
        val request = call.receive<RefreshTokenRequest>()
        userService.refreshToken(request.token)
            ?.let {
                call.respond(RefreshTokenResponse(it))
            } ?: return@post call.respond(HttpStatusCode.Unauthorized)

    }
}
package com.coderpwh.routing

import com.coderpwh.service.JwtService
import com.coderpwh.service.UserService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    userService: UserService
) {
    routing {
        route("/api/auth") {
            authRoute(userService)
        }

        route("/api/user") {
            userRoute(userService)
        }
    }
}
package com.coderpwh

import com.coderpwh.plugins.configureSecurity
import com.coderpwh.plugins.configureSerialzation
import com.coderpwh.repository.RefreshTokenRepository
import com.coderpwh.repository.UserRepository
import com.coderpwh.routing.configureRouting
import com.coderpwh.service.JwtService
import com.coderpwh.service.UserService
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val userRepository = UserRepository()
    val refreshTokenRepository = RefreshTokenRepository()
    val jwtService= JwtService(this,userRepository)
    val userService = UserService(userRepository,jwtService, refreshTokenRepository)
    configureSerialzation()
    configureSecurity(jwtService)
    configureRouting(userService)
}

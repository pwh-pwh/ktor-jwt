package com.coderpwh

import com.coderpwh.plugins.configureSerialzation
import com.coderpwh.repository.UserRepository
import com.coderpwh.routing.configureRouting
import com.coderpwh.service.UserService
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialzation()
    configureRouting(UserService(UserRepository()))
}

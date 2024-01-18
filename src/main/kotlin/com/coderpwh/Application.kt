package com.coderpwh

import com.coderpwh.plugins.configureSerialzation
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialzation()
}

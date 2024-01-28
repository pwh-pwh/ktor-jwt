package com.coderpwh.util

import com.coderpwh.plugins.RoleBasedConfigurationPlugin
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*

fun Route.authorized(
    vararg hasAnyRole:String,
    build: Route.() -> Unit
) {
    install(RoleBasedConfigurationPlugin) {
        roles = hasAnyRole.toSet()
    }
}
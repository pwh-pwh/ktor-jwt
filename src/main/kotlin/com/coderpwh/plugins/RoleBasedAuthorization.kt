package com.coderpwh.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

class PluginConfiguration {
    var roles = emptySet<String>()
}

val RoleBasedConfigurationPlugin = createRouteScopedPlugin(
    name = "RoleBasedPlugin",
    createConfiguration = ::PluginConfiguration
) {
    val roles = pluginConfig.roles
    pluginConfig.apply {
        on(AuthenticationChecked) {
            call ->
            val tokenRole = getRoleFromToken(call)
            val authorized = roles.contains(tokenRole)
            if (!authorized) {
                call.respond(HttpStatusCode.Forbidden)
            }
        }
    }
}

private fun getRoleFromToken(call: ApplicationCall) = call.principal<JWTPrincipal>()
    ?.payload
    ?.getClaim("role")
    ?.asString()
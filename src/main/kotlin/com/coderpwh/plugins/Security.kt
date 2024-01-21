package com.coderpwh.plugins

import com.coderpwh.service.JwtService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity(
    jwtService: JwtService
) {
    authentication {
        jwt {
            realm = jwtService.realm
            verifier(jwtService.jwtVerifier)
            validate {
                jwtCredential ->
                jwtService.customValidator(jwtCredential)
            }
        }

        jwt("another") {
            realm = jwtService.realm
            verifier(jwtService.jwtVerifier)
            validate {
                    jwtCredential ->
                jwtService.customValidator(jwtCredential)
            }
        }
    }
}
package com.coderpwh.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.coderpwh.routing.request.LoginRequest
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.util.Date

class JwtService(
    val application: Application,
    val userService: UserService
) {

    private val secret = getConfigProperty("jwt.secret")
    private val audience = getConfigProperty("jwt.audience")
    val realm = getConfigProperty("jwt.realm")
    private val issure = getConfigProperty("jwt.issure")

    val jwtVerifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(secret))
        .withIssuer(issure)
        .withAudience(audience)
        .build()

    fun createToken(loginRequest: LoginRequest): String? {
        var findByUserName = userService.findByUserName(loginRequest.username)
        return if (findByUserName != null && findByUserName.password == loginRequest.password) {
            JWT
                .create()
                .withAudience(audience)
                .withIssuer(issure)
                .withClaim("username", findByUserName.username)
                .withExpiresAt(Date(System.currentTimeMillis() + 3600000))
                .sign(Algorithm.HMAC256(secret))
        } else {
            null
        }
    }

    fun customValidator(credential: JWTCredential):JWTPrincipal? {
        var extractUserName = extractUserName(credential)
        val foundUser = extractUserName?.let(userService::findByUserName)
        return foundUser?.let {
            if (audienceMatches(credential)) {
                JWTPrincipal(credential.payload)
            } else null
        }
    }


    private fun audienceMatches(credential: JWTCredential) = credential.payload.audience.contains(audience)

    private fun extractUserName(credential:JWTCredential):String? = credential.payload.getClaim("username").asString()

    private fun getConfigProperty(path: String) =
        application.environment.config
            .property(path).getString()
}
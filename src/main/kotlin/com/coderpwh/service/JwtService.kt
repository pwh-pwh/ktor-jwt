package com.coderpwh.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.coderpwh.repository.UserRepository
import com.coderpwh.routing.request.LoginRequest
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.util.Date

class JwtService(
    val application: Application,
    val userRepository: UserRepository
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

    fun createAccessToken(username:String,role:String) = createToken(username,role,36_0000)

    fun createRefreshToken(username:String,role:String) = createToken(username,role,24 * 60 * 60)

    private fun createToken(username:String,
        role:String
                            ,expireIn:Int
    ): String =
            JWT
                .create()
                .withAudience(audience)
                .withIssuer(issure)
                .withClaim("username", username)
                .withClaim("role",role)
                .withExpiresAt(Date(System.currentTimeMillis() + expireIn))
                .sign(Algorithm.HMAC256(secret))


    fun customValidator(credential: JWTCredential):JWTPrincipal? {
        var extractUserName = extractUserName(credential)
        val foundUser = extractUserName?.let(userRepository::findByName)
        return foundUser?.let {
            if (audienceMatches(credential)) {
                JWTPrincipal(credential.payload)
            } else null
        }
    }

    fun audienceMatches(audience:String) = audience == this.audience


    private fun audienceMatches(credential: JWTCredential) = credential.payload.audience.contains(audience)

    private fun extractUserName(credential:JWTCredential):String? = credential.payload.getClaim("username").asString()

    private fun getConfigProperty(path: String) =
        application.environment.config
            .property(path).getString()
}
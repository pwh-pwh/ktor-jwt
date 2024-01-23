package com.coderpwh.service

import com.auth0.jwt.interfaces.DecodedJWT
import com.coderpwh.model.User
import com.coderpwh.repository.RefreshTokenRepository
import com.coderpwh.repository.UserRepository
import com.coderpwh.routing.request.LoginRequest
import com.coderpwh.routing.response.AuthResponse
import java.util.UUID

class UserService(
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val refreshTokenRepository: RefreshTokenRepository
) {

    fun findAll() = userRepository.findAll()

    fun findByUserName(name: String) = userRepository.findByName(name)

    fun findById(id: String) = userRepository.findById(id = UUID.fromString(id))

    fun save(user: User): User? {
        val foundUser = findByUserName(user.username)
        return foundUser?.let {
            null
        } ?: run {
            userRepository.save(user)
            user
        }
    }

    fun authenticate(loginRequest: LoginRequest): AuthResponse? {
        return findByUserName(loginRequest.username)
            ?.let {
                if (it.password == loginRequest.password) {
                    val accessToken = jwtService.createAccessToken(it.username)
                    val refreshToken = jwtService.createRefreshToken(it.username)
                    refreshTokenRepository.save(refreshToken, it.username)
                    AuthResponse(accessToken, refreshToken)
                } else null
            }
    }

    fun refreshToken(token: String): String? {
        val verifyRefreshToken = verifyRefreshToken(token)
        val persistedUserName = refreshTokenRepository.findByToken(token)
        return if (verifyRefreshToken!=null && persistedUserName!=null) {
            val foundUser = userRepository.findByName(persistedUserName)
            val userName = verifyRefreshToken.getClaim("username")
            if (foundUser!=null && userName.asString() == foundUser.username) {
                jwtService.createAccessToken(foundUser.username)
            } else {
                null
            }
        } else {
            null
        }
    }

    fun verifyRefreshToken(token: String): DecodedJWT? {
        val decodedJWT = decodedJWT(token)
        return decodedJWT?.let {
            val audienceMatches = jwtService.audienceMatches(it.audience.first())
            if (audienceMatches)
                decodedJWT
            else
                null
        }
    }

    fun decodedJWT(token: String) = try {
        jwtService.jwtVerifier.verify(token)
    } catch (e: Exception) {
        null
    }
}
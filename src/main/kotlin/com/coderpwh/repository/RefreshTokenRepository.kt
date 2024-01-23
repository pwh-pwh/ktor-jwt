package com.coderpwh.repository

class RefreshTokenRepository {
    val tokens = mutableMapOf<String, String>()

    fun findByToken(token:String) = tokens[token]

    fun save(token:String,username:String) {
        tokens[token] = username
    }
}
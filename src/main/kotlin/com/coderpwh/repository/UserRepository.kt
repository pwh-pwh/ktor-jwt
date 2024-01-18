package com.coderpwh.repository

import com.coderpwh.model.User
import java.util.UUID

class UserRepository {
    private val users = mutableListOf<User>();

    fun findAll() = users

    fun findByName(name: String) = users
        .firstOrNull {
            it.username == name
        }

    fun findById(id:UUID) = users.firstOrNull {
        it.id == id
    }

    fun save(user:User) = users.add(user)
}
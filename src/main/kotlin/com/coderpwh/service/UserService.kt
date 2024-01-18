package com.coderpwh.service

import com.coderpwh.model.User
import com.coderpwh.repository.UserRepository
import java.util.UUID

class UserService(
    private val userRepository: UserRepository
) {

    fun findAll() = userRepository.findAll()

    fun findByUserName(name: String) = userRepository.findByName(name)

    fun findById(id: String) = userRepository.findById(id = UUID.fromString(id))

    fun save(user: User): User? {
        val foundUser = findByUserName(user.username)
        return foundUser?.let {
            userRepository.save(it)
            user
        } ?: null
    }
}
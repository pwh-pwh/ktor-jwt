package com.coderpwh.routing.request

import com.coderpwh.model.User
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import java.util.UUID

@Serializable
data class UserRequest(
    val username:String,
    val password:String
) {
    fun toModel(): User {
        return User(
            username=this.username,
            password = this.password,
            id = UUID.randomUUID(),
            role = "USER"
        )
    }
}
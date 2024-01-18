package com.coderpwh.routing.request

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
data class UserRequest(
    val username:String,
    val password:String
)
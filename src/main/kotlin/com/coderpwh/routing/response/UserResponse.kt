package com.coderpwh.routing.response

import com.coderpwh.util.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import java.util.UUID

@Serializable
data class UserResponse(
    val username:String,
    @Serializable(with = UUIDSerializer::class)
    val id:UUID
)

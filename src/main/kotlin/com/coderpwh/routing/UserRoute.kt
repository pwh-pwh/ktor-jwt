package com.coderpwh.routing

import com.coderpwh.model.User
import com.coderpwh.repository.UserRepository
import com.coderpwh.routing.request.UserRequest
import com.coderpwh.routing.response.UserResponse
import com.coderpwh.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoute(userService: UserService) {
    post {
        val userRequest = call.receive<UserRequest>()
        val createUser = userService.save(
            userRequest.toModel()
        ) ?: return@post call.respond(HttpStatusCode.BadRequest)
        call.response
            .header(
                "id",
                createUser.id.toString()
            )
        call.respond(message = HttpStatusCode.OK)
    }

    get {
        val users = userService.findAll()
        call.respond(message = users.map(User::toResponse))
    }

    get("/{id}") {
        val id:String = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
        val findById = userService.findById(id) ?: return@get call.respond(HttpStatusCode.NotFound)
        call.respond(message = findById.toResponse())
    }
}

private fun User.toResponse(): UserResponse {
    return UserResponse(
        username = this.username,
        id = this.id
    )
}
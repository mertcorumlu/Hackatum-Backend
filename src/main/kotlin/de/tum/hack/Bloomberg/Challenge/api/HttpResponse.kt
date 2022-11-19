package de.tum.hack.Bloomberg.Challenge.api

data class HttpResponse(
    var status: ResponseType,
    var message: String? = null
)

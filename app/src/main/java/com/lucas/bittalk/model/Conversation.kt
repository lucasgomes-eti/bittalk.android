package com.lucas.bittalk.model

/**
 * Created by lucas on 28/11/2017.
 */
data class Conversation (
        val idUser: String,
        val name: String,
        val lastMessage: String,
        val oneSignalUserId: String
) {
    constructor(): this(
            idUser = "",
            name = "",
            lastMessage = "",
            oneSignalUserId = ""
    )
}
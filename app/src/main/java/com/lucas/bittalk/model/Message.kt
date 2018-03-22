package com.lucas.bittalk.model

/**
 * Created by lucas on 24/11/2017.
 */
data class Message (
        val idUser: String,
        val message: String
) {
    constructor(): this(
            idUser = "",
            message = ""
    )
}
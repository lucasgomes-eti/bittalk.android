package com.lucas.bittalk.model

/**
 * Created by lucas on 21/11/2017.
 */
data class User(
        val id: String,
        val email: String,
        val name: String,
        var oneSignalUserId: String
) {
    constructor() : this(
            id = "",
            email = "",
            name = "",
            oneSignalUserId = ""
    )
}
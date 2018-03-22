package com.lucas.bittalk.helpers

import android.content.Intent
import com.lucas.bittalk.model.User

/**
 * Created by lucas on 24/11/2017.
 */
fun Intent.putExtra(user: User) {
    putExtra("id", user.id)
    putExtra("email", user.email)
    putExtra("name", user.name)
    putExtra("oneSignalUserId", user.oneSignalUserId)
}

fun Intent.getUserExtra() = User(getStringExtra("id"), getStringExtra("email"), getStringExtra("name"), getStringExtra("oneSignalUserId"))
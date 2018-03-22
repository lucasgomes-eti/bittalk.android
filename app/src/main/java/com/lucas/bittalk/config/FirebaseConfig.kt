package com.lucas.bittalk.config

import android.util.Base64
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.lucas.bittalk.model.User
import java.nio.charset.Charset

/**
 * Created by lucas on 21/11/2017.
 */
class FirebaseConfig {

    private val databaseReference = FirebaseDatabase.getInstance().reference
    private val authInstance = FirebaseAuth.getInstance()

    fun getDatabase() = databaseReference
    fun getAuth() = authInstance

    fun getCurrentUser(): DatabaseReference {
        return databaseReference.child("users")
                .child(Base64.encodeToString(authInstance.currentUser?.email?.toByteArray(Charset.defaultCharset()), Base64.DEFAULT).replace("\n", ""))
    }
}
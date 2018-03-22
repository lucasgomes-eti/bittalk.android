package com.lucas.bittalk.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.lucas.bittalk.R
import com.lucas.bittalk.config.FirebaseConfig
import com.lucas.bittalk.helpers.Preferences
import com.lucas.bittalk.model.User
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast
import java.nio.charset.Charset

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inputEmail.editText?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                if (text.isNotEmpty()) {
                    inputEmail.isErrorEnabled = false
                }
            }
        })

        inputPassword.editText?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                if (text.isNotEmpty()) {
                    inputPassword.isErrorEnabled = false
                }
            }
        })

        buttonSignIn.setOnClickListener {
            when {
                inputEmail.editText?.text.toString().isEmpty() && inputPassword.editText?.text.toString().isEmpty() -> {
                    inputEmail.error = getString(R.string.field_empty)
                    inputPassword.error = getString(R.string.field_empty)
                }
                inputEmail.editText?.text.toString().isEmpty() -> inputEmail.error = getString(R.string.field_empty)
                inputPassword.editText?.text.toString().isEmpty() -> inputPassword.error = getString(R.string.field_empty)
                else -> {
                    signIn(inputEmail.editText?.text.toString(), inputPassword.editText?.text.toString())
                    loaderLogin.visibility = View.VISIBLE
                }
            }
        }

        buttonSignUp.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signIn(email: String, password: String) {
        FirebaseConfig().getAuth()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, { task ->
                    if (task.isSuccessful) {
                        toast(getString(R.string.success))
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        toast(task.exception?.message ?: getString(R.string.unknown_error))
                        loaderLogin.visibility = View.GONE
                    }
                })
    }
}

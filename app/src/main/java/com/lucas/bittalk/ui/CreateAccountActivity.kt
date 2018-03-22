package com.lucas.bittalk.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.View
import com.lucas.bittalk.R
import com.lucas.bittalk.config.FirebaseConfig
import com.lucas.bittalk.helpers.Preferences
import com.lucas.bittalk.model.User
import kotlinx.android.synthetic.main.activity_create_account.*
import org.jetbrains.anko.toast
import java.nio.charset.Charset

class CreateAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        inputName.editText?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                if (text.isNotEmpty()) {
                    inputName.isErrorEnabled = false
                }
            }
        })

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

        inputConfirmPassword.editText?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                if (text.isNotEmpty()) {
                    inputConfirmPassword.isErrorEnabled = false
                }
            }
        })

        buttonSignUp.setOnClickListener {
            when {
                inputName.editText?.text.toString().isEmpty() ||
                        inputEmail.editText?.text.toString().isEmpty() ||
                        inputPassword.editText?.text.toString().isEmpty() ||
                        inputConfirmPassword.editText?.text.toString().isEmpty() -> {
                    if (inputName.editText?.text.toString().isEmpty()) inputName.error = getString(R.string.field_empty)
                    if (inputEmail.editText?.text.toString().isEmpty()) inputEmail.error = getString(R.string.field_empty)
                    if (inputPassword.editText?.text.toString().isEmpty()) inputPassword.error = getString(R.string.field_empty)
                    if (inputConfirmPassword.editText?.text.toString().isEmpty()) inputConfirmPassword.error = getString(R.string.field_empty)
                }
                inputPassword.editText?.text.toString() != inputConfirmPassword.editText?.text.toString() -> {
                    inputPassword.error = getString(R.string.passwords_do_not_match)
                    inputConfirmPassword.error = getString(R.string.passwords_do_not_match)
                }
                else -> {
                    signUp(inputName.editText?.text.toString(), inputEmail.editText?.text.toString(), inputPassword.editText?.text.toString())
                    loaderCreateAccount.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun signUp(name: String, email: String, password: String) {
        FirebaseConfig().getAuth()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, { createUser ->
                    if (createUser.isSuccessful) {
                        toast(getString(R.string.success_on_create_user))
                        val user = User(Base64.encodeToString(email.toByteArray(Charset.defaultCharset()), Base64.DEFAULT).replace("\n", ""), email, name, Preferences(applicationContext).getOneSignalUserId())
                        FirebaseConfig().getDatabase()
                                .child("users")
                                .child(user.id)
                                .setValue(user)
                                .addOnCompleteListener(this, { saveUser ->
                                    if (saveUser.isSuccessful) {
                                        Preferences(applicationContext).saveUserPreferences(user.id)
                                        toast(getString(R.string.success_on_save_user))
                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        toast(saveUser.exception?.message ?: getString(R.string.unknown_error))
                                        loaderCreateAccount.visibility = View.GONE
                                    }
                                })
                    } else {
                        toast(createUser.exception?.message ?: getString(R.string.unknown_error))
                        loaderCreateAccount.visibility = View.GONE
                    }
                })
    }
}
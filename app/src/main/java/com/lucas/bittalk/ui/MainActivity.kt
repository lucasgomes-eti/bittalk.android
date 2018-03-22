package com.lucas.bittalk.ui

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.lucas.bittalk.R
import com.lucas.bittalk.config.FirebaseConfig
import com.lucas.bittalk.helpers.Preferences
import com.lucas.bittalk.model.User
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.elevation = 0.0f

        val tabApadter = TabsAdapter(this, supportFragmentManager)

        vpContent.adapter = tabApadter
        stlTabs.setupWithViewPager(vpContent)
        stlTabs.setTabTextColors(Color.argb(150,255,255,255), Color.WHITE)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_sign_out -> {
                MaterialDialog.Builder(this)
                        .title(getString(R.string.sign_out))
                        .content(getString(R.string.ask_sign_out))
                        .positiveText(getString(R.string.ok))
                        .onPositive { _, _ ->
                            try {
                                FirebaseConfig().getAuth()
                                        .signOut()
                                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                                }
                            catch (ex: Exception) {
                                toast(ex.message ?: getString(R.string.unknown_error))
                                return@onPositive
                            }
                        }
                        .negativeText(getString(R.string.cancel))
                        .show()
            }
            R.id.action_add_contact -> {
                MaterialDialog.Builder(this)
                        .title(getString(R.string.add_contact))
                        .content(getString(R.string.add_email_contact))
                        .inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT)
                        .input(getString(R.string.email), "", { _, _ ->  })
                        .positiveText(getString(R.string.ok))
                        .negativeText(getString(R.string.cancel))
                        .onPositive { dialog, _ ->
                            if (dialog.inputEditText?.text.toString().isNullOrEmpty()) {
                                toast(getString(R.string.email_empty))
                                return@onPositive
                            }
                            val emailBase64 = Base64.encodeToString(dialog.inputEditText?.text
                                    .toString()
                                    .toByteArray(Charset.defaultCharset()), Base64.DEFAULT)
                                    .replace("\n", "")
                            val firebase = FirebaseConfig().getDatabase()
                                    .child("users")
                                    .child(emailBase64)
                            firebase.addListenerForSingleValueEvent(object: ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                                    if (dataSnapshot?.value != null) {
                                        val user = dataSnapshot.getValue(User::class.java)
                                        FirebaseConfig().getDatabase()
                                                .child("contacts")
                                                .child(FirebaseConfig().getAuth().currentUser?.uid)
                                                .child(emailBase64)
                                                .setValue(user)
                                                .addOnCompleteListener(this@MainActivity, { task ->
                                                    if (task.isSuccessful) {
                                                        toast("${getString(R.string.new_contact)} ${user?.name}")
                                                    } else {
                                                        toast(task.exception?.message ?: getString(R.string.unknown_error))
                                                    }
                                                })

                                    } else {
                                        toast(getString(R.string.user_not_found))
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError?) {
                                    toast(databaseError?.message ?: getString(R.string.unknown_error))
                                }
                            })
                        }
                        .show()
            }
        }
        return false
    }
}

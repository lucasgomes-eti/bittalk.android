package com.lucas.bittalk.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.WindowManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.lucas.bittalk.R
import com.lucas.bittalk.config.FirebaseConfig
import com.lucas.bittalk.helpers.getUserExtra
import com.lucas.bittalk.model.Conversation
import com.lucas.bittalk.model.Message
import com.lucas.bittalk.model.Notification
import com.lucas.bittalk.model.User
import kotlinx.android.synthetic.main.activity_conversation.*
import org.jetbrains.anko.toast
import org.json.JSONException
import com.onesignal.OneSignal



class ConversationActivity : AppCompatActivity() {

    var currentUser: User? = null

    val messagesAdapter: MessagesAdapter by lazy {
        MessagesAdapter(mutableListOf(), currentUser)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        val user = intent.getUserExtra()
        val layoutManager = LinearLayoutManager(this)
        recyclerViewMessages.layoutManager = layoutManager
        recyclerViewMessages.setHasFixedSize(true)
        recyclerViewMessages.adapter = messagesAdapter
        FirebaseConfig().getCurrentUser()
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        currentUser = dataSnapshot?.getValue(User::class.java) ?: User()
                        messagesAdapter.currentUser = currentUser
                        FirebaseConfig().getDatabase()
                                .child("messages")
                                .child(currentUser?.id ?: return)
                                .child(user.id)
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                                        messagesAdapter.messages?.clear()
                                        dataSnapshot?.children?.forEach {
                                            val message = it.getValue(Message::class.java)
                                            messagesAdapter.messages?.add(message ?: return)
                                            messagesAdapter.notifyDataSetChanged()
                                            recyclerViewMessages.smoothScrollToPosition(messagesAdapter.messages!!.size -1)
                                        }

                                    }

                                    override fun onCancelled(databaseError: DatabaseError?) {
                                        toast(databaseError?.message ?: getString(R.string.unknown_error))
                                    }
                                })
                    }

                    override fun onCancelled(databaseError: DatabaseError?) {
                        toast(databaseError?.message ?: getString(R.string.unknown_error))
                    }
                })
        supportActionBar?.title = user.name


        fabSendMessage.setOnClickListener {
            if(editMessage.text.toString().isNotEmpty()) {
                FirebaseConfig().getDatabase()
                        .child("messages")
                        .child(currentUser?.id)
                        .child(user.id)
                        .push()
                        .setValue(Message(currentUser?.id ?: return@setOnClickListener, editMessage.text.toString()))
                FirebaseConfig().getDatabase()
                        .child("messages")
                        .child(user.id)
                        .child(currentUser?.id)
                        .push()
                        .setValue(Message(currentUser?.id ?: return@setOnClickListener, editMessage.text.toString()))
                        .addOnCompleteListener{ _ ->
                            val content = editMessage.text.toString()
                            val name = currentUser?.name ?: ""
                            val playerId = user.oneSignalUserId
                            editMessage.setText("")
                            val notification = Notification("003e0214-ded5-4690-879c-e332866b3d8d",
                                    arrayOf(playerId),
                                    hashMapOf("foo" to "bar"),
                                    hashMapOf("en" to content),
                                    hashMapOf("en" to name),
                                    hashMapOf("en" to getString(R.string.unread_messages)),
                                    hashMapOf("en" to getString(R.string.new_messages)))
                            try {
                                val notificationJson = Gson().toJson(notification)
                                OneSignal.postNotification(notificationJson, null)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }

                        }
                FirebaseConfig().getDatabase()
                        .child("conversations")
                        .child(currentUser?.id)
                        .child(user.id)
                        .setValue(Conversation(user.id, user.name, editMessage.text.toString(), user.oneSignalUserId))
                FirebaseConfig().getDatabase()
                        .child("conversations")
                        .child(user.id)
                        .child(currentUser?.id)
                        .setValue(Conversation(currentUser?.id ?: return@setOnClickListener,
                                currentUser?.name ?: return@setOnClickListener,
                                editMessage.text.toString(),
                                currentUser?.oneSignalUserId ?: return@setOnClickListener))

                messagesAdapter.messages?.add(Message(currentUser?.id ?: return@setOnClickListener, editMessage.text.toString()))
                messagesAdapter.notifyDataSetChanged()
                recyclerViewMessages.smoothScrollToPosition(messagesAdapter.messages!!.size -1)
            }
        }
    }
}
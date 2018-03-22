package com.lucas.bittalk.ui


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import com.lucas.bittalk.R
import com.lucas.bittalk.config.FirebaseConfig
import com.lucas.bittalk.helpers.Preferences
import com.lucas.bittalk.helpers.putExtra
import com.lucas.bittalk.model.Conversation
import com.lucas.bittalk.model.User
import kotlinx.android.synthetic.main.fragment_conversations.view.*
import org.jetbrains.anko.support.v4.toast
import java.nio.charset.Charset


/**
 * A simple [Fragment] subclass.
 */
class ConversationsFragment : Fragment() {

    var currentUser: User? = null

    val conversationsAdapter: ConversationsAdapter by lazy {
        ConversationsAdapter(mutableListOf(), { conversationClick(it) })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_conversations, container, false)

        val layoutManager = LinearLayoutManager(container?.context)
        view.recyclerViewConversations.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(view.recyclerViewConversations.context,
                layoutManager.orientation)
        view.recyclerViewConversations.addItemDecoration(dividerItemDecoration)
        view.recyclerViewConversations.adapter = conversationsAdapter
        return view
    }

    override fun onStart() {
        super.onStart()
        FirebaseConfig().getCurrentUser()
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        currentUser = dataSnapshot?.getValue(User::class.java) ?: User()
                        FirebaseConfig()
                                .getDatabase()
                                .child("conversations")
                                .child(currentUser?.id)
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                                        conversationsAdapter.conversations?.clear()
                                        dataSnapshot?.children?.forEach {
                                            val conversation = it.getValue(Conversation::class.java)
                                            conversationsAdapter.conversations?.add(conversation ?: return@forEach)
                                            conversationsAdapter.notifyDataSetChanged()
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

    }

    fun conversationClick(conversation: Conversation) {
        val intent = Intent(this.context, ConversationActivity::class.java)
        intent.putExtra(User(conversation.idUser, String(Base64.decode(conversation.idUser, Base64.DEFAULT)), conversation.name, conversation.oneSignalUserId))
        startActivity(intent)
    }
}

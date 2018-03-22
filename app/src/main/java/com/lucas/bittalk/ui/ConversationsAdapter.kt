package com.lucas.bittalk.ui

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lucas.bittalk.R
import com.lucas.bittalk.model.Conversation

/**
 * Created by lucas on 28/11/2017.
 */
class ConversationsAdapter(val conversations: MutableList<Conversation>?, val conversationClick: (Conversation) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_conversation, parent, false)
        return ConversationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is ConversationViewHolder) {
            val conversation = conversations?.get(position) ?: return
            holder.name.text = conversation.name
            holder.lastMessage.text = conversation.lastMessage
            holder.layout.setOnClickListener {
                conversationClick(conversation)
            }
        }
    }

    override fun getItemCount() = conversations?.size ?: 0

    private class ConversationViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView by lazy { itemView.findViewById<TextView>(R.id.textUserNameConversation) }
        val lastMessage: TextView by lazy { itemView.findViewById<TextView>(R.id.textLastMessage) }
        val layout: ConstraintLayout by lazy { itemView.findViewById<ConstraintLayout>(R.id.constraintLayoutItemConversation) }
    }
}
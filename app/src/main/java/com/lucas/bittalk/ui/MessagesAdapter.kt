package com.lucas.bittalk.ui

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lucas.bittalk.R
import com.lucas.bittalk.model.Message
import com.lucas.bittalk.model.User

/**
 * Created by lucas on 27/11/2017.
 */
class MessagesAdapter(val messages: MutableList<Message>?, var currentUser: User?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is MessageViewHolder) {
            val message = messages?.get(position) ?: return
            holder.message.text = message.message
            if (message.idUser != currentUser?.id) {
                holder.card.setCardBackgroundColor(Color.argb(255,238,238,238))
                holder.message.setTextColor(Color.BLACK)
                holder.view.gravity = Gravity.START
            } else {
                holder.card.setCardBackgroundColor(holder.itemView.context.resources.getColor(R.color.colorPrimary))
                holder.message.setTextColor(Color.WHITE)
                holder.view.gravity = Gravity.END
            }
        }
    }

    override fun getItemCount() = messages?.size ?: 0

    private class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message: TextView by lazy { itemView.findViewById<TextView>(R.id.textMessage) }
        val view: android.support.v7.widget.LinearLayoutCompat by lazy { itemView.findViewById<android.support.v7.widget.LinearLayoutCompat>(R.id.linearLayoutMessage) }
        val card: android.support.v7.widget.CardView by lazy { itemView.findViewById<android.support.v7.widget.CardView>(R.id.cardViewMessage) }
    }
}
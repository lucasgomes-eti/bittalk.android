package com.lucas.bittalk.ui

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lucas.bittalk.R
import com.lucas.bittalk.model.User

/**
 * Created by lucas on 23/11/2017.
 */
class ContactsAdapter(val contacts: MutableList<User>?, val contactClick: (User) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_contact, parent, false)
        return ContactsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is ContactsViewHolder) {
            val contact = contacts?.get(position) ?: return
            holder.name.text = contact.name
            holder.itemLayout.setOnClickListener {
                contactClick(contact)
            }
        }
    }

    override fun getItemCount() = contacts?.size ?: 0

    private class ContactsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView by lazy { itemView.findViewById<TextView>(R.id.textUserName) }
        val itemLayout: ConstraintLayout by lazy { itemView.findViewById<ConstraintLayout>(R.id.itemContact) }
    }
}
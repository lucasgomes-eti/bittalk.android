package com.lucas.bittalk.ui


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.lucas.bittalk.R
import com.lucas.bittalk.config.FirebaseConfig
import com.lucas.bittalk.helpers.putExtra
import com.lucas.bittalk.model.User
import kotlinx.android.synthetic.main.fragment_contacts.view.*
import org.jetbrains.anko.support.v4.toast


/**
 * A simple [Fragment] subclass.
 */
class ContactsFragment : Fragment() {

    val contactsAdapter: ContactsAdapter by lazy {
        ContactsAdapter(mutableListOf(), { contactClick(it) })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_contacts, container, false)

        val layoutManager = LinearLayoutManager(container?.context)
        view.recyclerViewContacts.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(view.recyclerViewContacts.context,
                layoutManager.orientation)
        view.recyclerViewContacts.addItemDecoration(dividerItemDecoration)
        view.recyclerViewContacts.adapter = contactsAdapter

        return view
    }

    override fun onStart() {
        super.onStart()
        FirebaseConfig()
                .getDatabase()
                .child("contacts")
                .child(FirebaseConfig().getAuth().currentUser?.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        contactsAdapter.contacts?.clear()
                        dataSnapshot?.children?.forEach {
                            val user = it.getValue(User::class.java)
                            contactsAdapter.contacts?.add(user ?: return@forEach)
                            contactsAdapter.notifyDataSetChanged()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError?) {
                        toast(databaseError?.message ?: getString(R.string.unknown_error))
                    }
                })
    }

    fun contactClick(user: User) {
        val intent = Intent(this.context, ConversationActivity::class.java)
        intent.putExtra(user)
        startActivity(intent)
    }
}

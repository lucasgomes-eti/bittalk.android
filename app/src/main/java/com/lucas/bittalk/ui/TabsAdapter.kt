package com.lucas.bittalk.ui

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.lucas.bittalk.R

/**
 * Created by lucas on 22/11/2017.
 */
class TabsAdapter constructor(private val context: Context,
                              private val fragmentManager: FragmentManager
                              ) : FragmentStatePagerAdapter(fragmentManager) {

    val tabsTitle = arrayListOf(context.getString(R.string.conversations), context.getString(R.string.contacts))

    override fun getItem(position: Int): Fragment = when(position) {
        0 -> ConversationsFragment()
        1 -> ContactsFragment()
        else -> {
            ConversationsFragment()
        }
    }

    override fun getCount() = tabsTitle.size

    override fun getPageTitle(position: Int) = tabsTitle[position]
}
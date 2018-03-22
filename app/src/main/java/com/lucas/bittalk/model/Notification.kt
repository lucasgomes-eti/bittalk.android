package com.lucas.bittalk.model

/**
 * Created by lucas on 29/11/2017.
 */
data class Notification(
        val app_id: String,
        val include_player_ids: Array<String>,
        val data: HashMap<String, String>,
        val contents: HashMap<String, String>,
        val headings: HashMap<String, String>,
        val android_group: HashMap<String, String>,
        val android_group_message: HashMap<String, String>,
        val small_icon: String = "ic_stat_chat"
)
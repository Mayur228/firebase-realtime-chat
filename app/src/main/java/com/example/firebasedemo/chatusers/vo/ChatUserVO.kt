package com.example.firebasedemo.chatusers.vo

data class ChatUserVO(
    val friendId: String = "",
    val userId: String = "",
    val userName: String = "",
    val userProfile: String = "",
    val lastMessage: String = "",
    val isFriend: Boolean = false
)

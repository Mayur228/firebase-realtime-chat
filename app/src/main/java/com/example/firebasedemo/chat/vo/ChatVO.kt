package com.example.firebasedemo.chat.vo

import java.util.*

data class ChatVO(
    val chatID: String = "",
    val senderId: String = "",
    val message: String = "",
    val date: Date = Date(),
    val user: UserVO = UserVO("","",""),
)

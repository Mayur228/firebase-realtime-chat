package com.example.firebasedemo.chat.vo

import com.stfalcon.chatkit.commons.models.IUser

class UserVO(
    val userId: String = "",
    val userName: String = "",
    val userProfileUrl: String = ""
) : IUser {
    override fun getId(): String {
        return userId
    }

    override fun getName(): String {
        return userName
    }

    override fun getAvatar(): String {
        return userProfileUrl
    }
}
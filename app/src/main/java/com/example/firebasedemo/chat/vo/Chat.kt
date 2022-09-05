package com.example.firebasedemo.chat.vo

import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.commons.models.IUser
import com.stfalcon.chatkit.commons.models.MessageContentType
import java.text.DateFormat
import java.util.*


data class Chat constructor(
    var chatId:String = "",
    var message: String = "",
    var date: Date = Date(),
    var userVO: UserVO = UserVO("","",""),
    var imageMessage: String? = null
): IMessage, MessageContentType.Image {
    override fun getId(): String {
        return chatId
    }

    override fun getText(): String {
        return message
    }

    override fun getUser(): IUser {
        return userVO
    }

    override fun getCreatedAt(): Date {
        return date
    }

    override fun getImageUrl(): String? {
        return imageMessage
    }
}
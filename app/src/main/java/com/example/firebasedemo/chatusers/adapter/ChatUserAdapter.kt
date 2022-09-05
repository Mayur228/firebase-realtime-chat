package com.example.firebasedemo.chatusers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedemo.R
import com.example.firebasedemo.chat.vo.Chat
import com.example.firebasedemo.chat.vo.UserVO
import com.example.firebasedemo.chatusers.adapter.viewholder.ChatUserVH
import com.example.firebasedemo.chatusers.vo.ChatUserVO
import javax.inject.Inject

class ChatUserAdapter @Inject constructor(
    private val onItemClick: (userId: String, userName: String, userProfile: String) -> Unit
) : RecyclerView.Adapter<ChatUserVH>() {
    var userList: List<ChatUserVO> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatUserVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_user,parent,false)
        return ChatUserVH(view)
    }

    override fun onBindViewHolder(holder: ChatUserVH, position: Int) {
        holder.bind(userList[position],onItemClick)
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}
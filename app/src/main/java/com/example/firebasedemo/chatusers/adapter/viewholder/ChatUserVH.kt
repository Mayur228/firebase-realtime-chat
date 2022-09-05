package com.example.firebasedemo.chatusers.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasedemo.R
import com.example.firebasedemo.chat.vo.Chat
import com.example.firebasedemo.chat.vo.UserVO
import com.example.firebasedemo.chatusers.vo.ChatUserVO
import de.hdodenhof.circleimageview.CircleImageView

class ChatUserVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: ChatUserVO,onItemClick:(userId: String,userName:String,userProfile: String) -> Unit) {
        val profileIV = itemView.findViewById<CircleImageView>(R.id.userProfileIV)
        val userName = itemView.findViewById<TextView>(R.id.userNameTV)
        val lastMessage = itemView.findViewById<TextView>(R.id.lastMessageTV)


        Glide.with(itemView.context).load(item.userProfile).into(profileIV)
        userName.text = item.userName
        lastMessage.text = item.lastMessage

        itemView.setOnClickListener {
            onItemClick(item.userId,item.userName,item.userProfile)
        }
    }
}
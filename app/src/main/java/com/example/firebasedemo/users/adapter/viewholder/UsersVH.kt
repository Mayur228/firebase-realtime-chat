package com.example.firebasedemo.users.adapter.viewholder

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasedemo.R
import com.example.firebasedemo.users.vo.UserVO
import de.hdodenhof.circleimageview.CircleImageView

class UsersVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: UserVO,onItemClick:(userId: String,userName:String,userProfile: String) -> Unit) {
        val profileIV = itemView.findViewById<CircleImageView>(R.id.userProfileIV)
        val userName = itemView.findViewById<TextView>(R.id.userNameTV)
        val lastMessage = itemView.findViewById<TextView>(R.id.lastMessageTV)
        val constraintL = itemView.findViewById<ConstraintLayout>(R.id.constraintL)

        Glide.with(itemView.context).load(item.userProfile).into(profileIV)
        userName.text = item.userName
        lastMessage.isVisible = false

        constraintL.setOnClickListener {
            onItemClick(
                item.userID,
                item.userName,
                item.userProfile
            )
        }
    }
}
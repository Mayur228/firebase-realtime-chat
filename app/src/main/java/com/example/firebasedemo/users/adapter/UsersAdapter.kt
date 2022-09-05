package com.example.firebasedemo.users.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedemo.R
import com.example.firebasedemo.users.vo.UserVO
import com.example.firebasedemo.users.adapter.viewholder.UsersVH
import javax.inject.Inject

class UsersAdapter @Inject constructor(
    private val onItemClick: (userId: String, userName: String, userProfile: String) -> Unit
) :
    RecyclerView.Adapter<UsersVH>() {
    var userList: List<UserVO> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_chat_user, parent, false)
        return UsersVH(view)
    }

    override fun onBindViewHolder(holder: UsersVH, position: Int) {
        holder.bind(userList[position], onItemClick)
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}
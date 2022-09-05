package com.example.firebasedemo.chatusers

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasedemo.R
import com.example.firebasedemo.chat.ChatActivity
import com.example.firebasedemo.chat.vo.Chat
import com.example.firebasedemo.chatusers.adapter.ChatUserAdapter
import com.example.firebasedemo.chatusers.vo.ChatUserVO
import com.example.firebasedemo.databinding.ActivityChatUserListBinding
import com.example.firebasedemo.users.UserListActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatUserListActivity : AppCompatActivity() {
    companion object {
        val TAG = ChatUserListActivity::class.java.simpleName
        val USER_LIST = "$TAG.USER_LIST"
        fun startActivity(context: Context, userList: ArrayList<Chat>): Intent {
            return Intent(context, ChatUserListActivity::class.java).apply {
                putExtra(USER_LIST, userList)
            }
        }
    }

    lateinit var adapter: ChatUserAdapter
    lateinit var binding: ActivityChatUserListBinding
    private lateinit var db: DatabaseReference
    val friendList = mutableListOf<ChatUserVO>()

    val onItemClicked: (userID: String, userName: String, userProfile: String) -> Unit =
        { id: String, name: String, profile: String ->
           startActivity(ChatActivity.startActivity(applicationContext,id,name,profile))
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_user_list)

        setUpView()
    }

    private fun setUpView() {
        adapter = ChatUserAdapter(onItemClicked)
        binding.userRV.layoutManager = LinearLayoutManager(applicationContext)
        binding.userRV.adapter = adapter

        db = Firebase.database.getReferenceFromUrl("https://fir-demo-49889-default-rtdb.firebaseio.com/")

        db.child("friends").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                friendList.clear()
                for (user in snapshot.children) {
                    val friends = user.getValue(ChatUserVO::class.java)

                    if (friends != null && friends.friendId.startsWith(Firebase.auth.currentUser?.uid.toString(),true)) {
                        friendList.add(friends)
                    }
                }
                adapter.userList = friendList
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

//        binding.addUserBtn.isVisible = !friendList.isNullOrEmpty()

        binding.addUserBtn.setOnClickListener {
            startActivity(
                UserListActivity.startActivity(
                context = applicationContext,
                userID = Firebase.auth.currentUser?.uid.toString(),
                userName = Firebase.auth.currentUser?.displayName.toString(),
                userProfile = Firebase.auth.currentUser?.photoUrl.toString()))
        }

    }
}
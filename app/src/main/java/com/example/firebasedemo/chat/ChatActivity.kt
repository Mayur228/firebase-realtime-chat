package com.example.firebasedemo.chat

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.firebasedemo.chat.vo.Chat
import com.example.firebasedemo.chat.vo.UserVO
import com.example.firebasedemo.R
import com.example.firebasedemo.chat.vo.ChatVO
import com.example.firebasedemo.chatusers.ChatUserListActivity
import com.example.firebasedemo.chatusers.vo.ChatUserVO
import com.example.firebasedemo.databinding.ActivityChatBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.messages.MessageHolders
import com.stfalcon.chatkit.messages.MessagesListAdapter
import java.time.LocalDateTime
import java.util.*

class ChatActivity : AppCompatActivity() {
    companion object {
        val TAG = ChatActivity::class.java.simpleName
        val EXTRA_USER_ID = "$TAG.EXTRA_USER_ID"
        val EXTRA_NAME = "$TAG.EXTRA_NAME"
        val EXTRA_PHOTO_URL = "$TAG.EXTRA_PHOTO_URL"
        fun startActivity(
            context: Context,
            userId: String,
            userName: String,
            userProfile: String
        ): Intent {
            return Intent(context, ChatActivity::class.java).apply {
                putExtra(EXTRA_USER_ID, userId)
                putExtra(EXTRA_NAME, userName)
                putExtra(EXTRA_PHOTO_URL, userProfile)
            }
        }

    }

    lateinit var binding: ActivityChatBinding
    private lateinit var db: DatabaseReference
    lateinit var adapter: MessagesListAdapter<Chat>
    val chatList = mutableListOf<Chat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)

        setUpFirebaseDB()
        setUpView()
    }

    private fun setUpFirebaseDB() {
        db =
            Firebase.database.getReferenceFromUrl("https://fir-demo-49889-default-rtdb.firebaseio.com/")
    }

    private fun setUpView() {
        val imageLoader = ImageLoader { imageView, url, payload ->
            if (imageView != null) {
                Glide.with(applicationContext).load(url).centerCrop().into(imageView)
            }
        }

        adapter = MessagesListAdapter(Firebase.auth.currentUser?.uid, MessageHolders(), imageLoader)
        binding.chatView.setAdapter(adapter)

        val sendRoomID = Firebase.auth.currentUser?.uid + intent.getStringExtra(EXTRA_USER_ID)
        val receiverID = intent.getStringExtra(EXTRA_USER_ID) + Firebase.auth.currentUser?.uid

        db.child("chat_room").child(sendRoomID).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                adapter.clear()
                for (chats in snapshot.children) {
                    val chat = chats.getValue(ChatVO::class.java)

                    if (chat != null) {
                        chatList.add(
                            Chat(
                                chatId = chat.chatID,
                                message = chat.message,
                                date = chat.date,
                                userVO = chat.user
                            )
                        )
                    }
                }
                val a = chatList.sortedBy { it.createdAt }

                if(!a.isNullOrEmpty()) {
                    val receiverUser = ChatUserVO(
                        friendId = sendRoomID,
                        userId = intent.getStringExtra(EXTRA_USER_ID) ?: "",
                        userName = intent.getStringExtra(EXTRA_NAME) ?: "",
                        userProfile = intent.getStringExtra(EXTRA_PHOTO_URL) ?: "",
                        lastMessage = a.last().message,
                        isFriend = true
                    )
                    db.child("friends").child(sendRoomID)
                        .setValue(receiverUser)

                    val senderUser = ChatUserVO(
                        friendId = receiverID,
                        userId = Firebase.auth.currentUser?.uid ?: "",
                        userName = Firebase.auth.currentUser?.displayName ?: "",
                        userProfile = Firebase.auth.currentUser?.photoUrl.toString() ?: "",
                        lastMessage = a.last().message,
                        isFriend = true
                    )
                    db.child("friends").child(receiverID)
                        .setValue(senderUser)

                }

                adapter.addToEnd(a, true)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }

        })

        binding.messageIN.button.setOnClickListener {
            addMessageToDb(binding.messageIN.inputEditText.text.toString())
            binding.messageIN.inputEditText.setText("")
        }

        binding.attachmentIB.setOnClickListener {
            startActivity(Intent(applicationContext, ChatUserListActivity::class.java))
        }

    }

    private fun addMessageToDb(message: String) {
        val sendRoomID = Firebase.auth.currentUser?.uid + intent.getStringExtra(EXTRA_USER_ID)
        val receiverRoomID = intent.getStringExtra(EXTRA_USER_ID) + Firebase.auth.currentUser?.uid
        val user = UserVO(
            userId = Firebase.auth.currentUser?.uid ?: "u1",
            userName = Firebase.auth.currentUser?.displayName ?: "abc",
            userProfileUrl = Firebase.auth.currentUser?.photoUrl.toString()
        )

        val chatId = UUID.randomUUID().toString()

        val chat = ChatVO(
            chatID = chatId,
            senderId = intent.getStringExtra(EXTRA_USER_ID).toString(),
            message = message,
            date = Date(),
            user = user
        )

        db.child("chat_room").child(sendRoomID).push().setValue(chat).addOnSuccessListener {
            db.child("chat_room").child(receiverRoomID).push().setValue(chat)
        }
    }
}
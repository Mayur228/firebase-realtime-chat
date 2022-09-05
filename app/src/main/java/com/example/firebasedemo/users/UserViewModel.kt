package com.example.firebasedemo.users

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.firebasedemo.chat.ChatActivity
import com.example.firebasedemo.chat.vo.Chat
import com.example.firebasedemo.chatusers.vo.ChatUserVO
import com.example.firebasedemo.users.vo.UserVO
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    init {
        addUserToDB(
            savedStateHandle.get<String>(UserListActivity.EXTRA_USER_ID).toString(),
            savedStateHandle.get<String>(UserListActivity.EXTRA_USER_NAME).toString(),
            savedStateHandle.get<String>(UserListActivity.EXTRA_USER_PROFILE).toString()
        )

        loadData()
    }
    val userList = MutableLiveData<List<UserVO>>()
    val redirectToChatEvent = LiveEvent<UserVO>()

    private fun addUserToDB(userID: String, userName: String, userProfile: String) {
        val db = Firebase.database.getReferenceFromUrl("https://fir-demo-49889-default-rtdb.firebaseio.com/")

        val user = UserVO(
            userID = userID,
            userName = userName,
            userProfile = userProfile
        )
        db.child("user").child(userID).setValue(user)
    }

    private fun loadData() {
        val db = Firebase.database.getReferenceFromUrl("https://fir-demo-49889-default-rtdb.firebaseio.com/")

        val users = mutableListOf<UserVO>()
        val userList = object : ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                users.clear()
                for (chats in snapshot.children) {
                    val user = chats.getValue(UserVO::class.java)

                    if (user != null) {
                        users.add(user)
                    }

                }
                userList.value = users.filter {
                    Firebase.auth.currentUser?.uid != it.userID
                }

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ChatActivity.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        db.addChildEventListener(userList)
    }

    fun redirectToChat(id: String,userName: String,userProfile: String) {
        redirectToChatEvent.value = UserVO(
            userID = id,
            userName = userName,
            userProfile = userProfile
        )
    }
}
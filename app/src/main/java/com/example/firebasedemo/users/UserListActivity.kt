package com.example.firebasedemo.users

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasedemo.R
import com.example.firebasedemo.chat.ChatActivity
import com.example.firebasedemo.databinding.ActivityUserListBinding
import com.example.firebasedemo.users.adapter.UsersAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class UserListActivity : AppCompatActivity() {
    companion object {
        val TAG = UserListActivity::class.java.simpleName
        val EXTRA_USER_ID = "$TAG.EXTRA_USER_ID"
        val EXTRA_USER_NAME = "$TAG.EXTRA_USER_NAME"
        val EXTRA_USER_PROFILE = "$TAG.EXTRA_USER_PROFILE"

        fun startActivity(
            context: Context,
            userID: String,
            userName: String,
            userProfile: String,
        ): Intent {
            return Intent(context, UserListActivity::class.java).apply {
                putExtra(EXTRA_USER_ID, userID)
                putExtra(EXTRA_USER_NAME, userName)
                putExtra(EXTRA_USER_PROFILE, userProfile)
            }
        }
    }

    lateinit var binding: ActivityUserListBinding
    val viewModel by viewModels<UserViewModel>()

    lateinit var adapter: UsersAdapter

    val onItemClicked: (userID: String, userName: String, userProfile: String) -> Unit =
        { id: String, name: String, profile: String ->
            viewModel.redirectToChat(id, name, profile)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_list)

        setUpView()
        observeEvent()
    }

    private fun setUpView() {
        adapter = UsersAdapter(onItemClicked)
        binding.userRV.layoutManager = LinearLayoutManager(applicationContext)
        binding.userRV.adapter = adapter

        viewModel.userList.observe(this, Observer {
            adapter.userList = it
        })
    }

    private fun observeEvent() {
        viewModel.redirectToChatEvent.observe(this, Observer {
            startActivity(
                ChatActivity.startActivity(
                    applicationContext,
                    userId = it.userID,
                    userName = it.userName,
                    userProfile = it.userProfile
                ))
            finish()
        })
    }
}
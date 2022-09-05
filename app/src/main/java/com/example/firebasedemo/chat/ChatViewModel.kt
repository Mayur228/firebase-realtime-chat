package com.example.firebasedemo.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.firebasedemo.chat.vo.UserVO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val me = UserVO(
        userId = "u1",
        userName = savedStateHandle.get<String>(ChatActivity.EXTRA_NAME) ?: "",
        userProfileUrl = savedStateHandle.get<String>(ChatActivity.EXTRA_PHOTO_URL) ?: ""
    )
}
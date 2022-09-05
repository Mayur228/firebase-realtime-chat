package com.example.firebasedemo.users.di

import android.app.Activity
import com.example.firebasedemo.users.UserListActivity
import com.example.firebasedemo.users.adapter.UsersAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class SelectUserModule {
    @Provides
    fun provideOnItemClicked(activity: Activity): UsersAdapter {
        activity as UserListActivity
        return UsersAdapter(
            onItemClick = activity.onItemClicked
        )
    }
}
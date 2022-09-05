package com.example.firebasedemo.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.firebasedemo.MainActivity
import com.example.firebasedemo.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.notification?.imageUrl != null) {
            createBitmap(message)
        }else {
            createNotification(message)
        }
    }

    private fun createBitmap(message: RemoteMessage) {
        Glide.with(applicationContext)
            .asBitmap()
            .load(message.notification?.imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    createNotification(
                        message = message,
                        imageBitmap = resource
                    )
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {

                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
    }

    private fun createNotification(message: RemoteMessage,imageBitmap: Bitmap? = null) {
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            Intent(applicationContext, MainActivity::class.java),
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        val notificationBuilder = if (message.data["data"].toString() == "big style notification") {
            NotificationCompat.Builder(
                applicationContext,
                getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(message.notification?.title)
                .setContentText(message.notification?.body)
                .setLargeIcon(imageBitmap)
                .setStyle(NotificationCompat.BigPictureStyle()
                    .bigPicture(imageBitmap))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
        }else {
            NotificationCompat.Builder(
                applicationContext,
                getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(message.notification?.title)
                .setContentText(message.notification?.body)
                .setLargeIcon(imageBitmap)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
        }

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =  NotificationChannel(
                getString(R.string.default_notification_channel_id),
                "android",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Test Android"
            }

            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(1,notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("Token", token)
    }

}
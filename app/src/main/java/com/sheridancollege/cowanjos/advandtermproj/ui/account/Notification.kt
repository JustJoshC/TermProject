package com.sheridancollege.cowanjos.advandtermproj.ui.account

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.sheridancollege.cowanjos.advandtermproj.R

const val notificationID = 1
const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"
class Notification : BroadcastReceiver() {
    @SuppressLint("NotificationPermission")
    override fun onReceive(context: Context, intent: Intent?) {

        //creating the notification layout
        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_account_icon)
            .setContentTitle(intent?.getStringExtra(titleExtra))
            .setContentText(intent?.getStringExtra(messageExtra))
            .build()

        //setting up the notification service
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // setting the notification
        manager.notify(notificationID, notification)



    }
}
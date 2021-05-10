package com.example.chuckapp.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.chuckapp.R
import com.example.chuckapp.model.FirebaseToken
import com.example.chuckapp.modules.call.CallReceiverActivity
import com.example.chuckapp.modules.home.service.HomeClient
import com.example.chuckapp.util.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response


private const val CHANNEL_ID = "my_channel"

class MyFirebaseInstanceIDService : FirebaseMessagingService() {

    private var broadcaster: LocalBroadcastManager? = null

    lateinit var notificationChannel: NotificationChannel
    lateinit var notificationManager: NotificationManager
    lateinit var builder: Notification.Builder
    private val channelId = "12345"
    private val description = "Test Notification"

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println(token)
        sendFirebaseToken(token)
    }


    override fun onMessageReceived(message: RemoteMessage) {
        val callReceiverIntent = Intent("CallReceiverData")
        when (message.data["title"]){

            "ONLINE", "OFFLINE" -> {
                val intent = Intent("MyData")
                intent.putExtra("userId", message.data["user_id"])
                intent.putExtra("action", message.data["title"])
                broadcaster!!.sendBroadcast(intent)
            }

            "ON_CALL_CREATE" -> {
                val intent = Intent(this, CallReceiverActivity::class.java)
                intent.putExtra("callerFullName", message.data["caller_full_name"])
                intent.putExtra("callId", message.data["call_id"])
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }

            "ON_CALL_DECLINED" -> {
                val intent = Intent("CallSenderData")
                intent.putExtra("action", message.data["title"])
                broadcaster!!.sendBroadcast(intent)
            }

            "ON_CALL_INTERRUPTED" -> {
                callReceiverIntent.putExtra("action", "ON_CALL_INTERRUPTED")
                broadcaster!!.sendBroadcast(callReceiverIntent)
                btnNotify(message.data["caller_full_name"].toString())
            }

            "ON_CALL_ACCEPTED" -> {
                val senderIntent = Intent("CallSenderData")
                val receiverFullName = message.data["receiver_full_name"].toString()
                val accessToken = message.data["access_token"].toString()
                val roomName = message.data["room_name"].toString()
                senderIntent.putExtra("action", "ON_CALL_ACCEPTED")
                senderIntent.putExtra("receiver_full_name", receiverFullName)
                senderIntent.putExtra("access_token", accessToken)
                senderIntent.putExtra("room_name", roomName)
                broadcaster!!.sendBroadcast(senderIntent)
            }
        }
    }

    private fun btnNotify(caller: String) {
        val intent = Intent(this, LauncherActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        notificationChannel =
            NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.enableVibration(true)
        notificationManager.createNotificationChannel(notificationChannel)
        builder = Notification.Builder(this, channelId).setContentTitle("1 Missing Call ")
            .setContentText(caller).setSmallIcon(R.drawable.ic_baseline_phone_missed_24)
            .setContentIntent(pendingIntent)
        notificationManager.notify(12345, builder.build())
    }

    private fun sendFirebaseToken(token: String) {
        HomeClient().getHomeApiService(this).sendFirebaseToken(FirebaseToken(token))
            .enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    println(response.code())
                    if (response.isSuccessful)
                        println("Token Yollandı")
                    else
                        println("token yollanamadı")
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Constants.showError(t)
                }

            })
    }


}
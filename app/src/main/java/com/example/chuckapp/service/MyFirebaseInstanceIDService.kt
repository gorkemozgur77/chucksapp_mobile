package com.example.chuckapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.chuckapp.model.FirebaseToken
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

    override fun onCreate() {
        super.onCreate()

        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println(token)
        sendFirebaseToken(token)
    }


    override fun onMessageReceived(message: RemoteMessage) {


        println(message.data["user_id"])
        println(message.data["message"])
        val intent = Intent("MyData")
        intent.putExtra("userId", message.data["user_id"])
        intent.putExtra("action", message.data["title"])
        broadcaster!!.sendBroadcast(intent)



//        val intent = Intent(this, MainActivity::class.java)
//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val notificationID = Random.nextInt()
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createNotificationChannel(notificationManager)
//        }
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
//        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setContentTitle(message.data["title"])
//            .setContentText(message.data["message"])
//            .setSmallIcon(R.drawable.ic_baseline_check_circle_24)
//            .setAutoCancel(true)
//            .setContentIntent(pendingIntent)
//            .build()
//
//        notificationManager.notify(notificationID, notification)
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "channelName"
        val channel = NotificationChannel(
            CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "My channel description"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun sendFirebaseToken(token: String) {
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
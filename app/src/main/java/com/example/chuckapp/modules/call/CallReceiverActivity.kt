package com.example.chuckapp.modules.call

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.chuckapp.R
import com.example.chuckapp.model.friend.Friend
import com.example.chuckapp.modules.home.service.HomeClient
import com.example.chuckapp.util.Constants
import kotlinx.android.synthetic.main.activity_call_reciever.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response


class CallReceiverActivity : AppCompatActivity() {

    private val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
    private lateinit var ringtone: Ringtone

    var friend = Friend("a", "a", "a")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_reciever)
        ringtone = RingtoneManager.getRingtone(applicationContext, notification)
        callerTextView.text = intent.getStringExtra("callerFullName")
        ringtone.play()
        acceptCallFAB.setOnClickListener {
            accept()
        }

        declineCallFAB.setOnClickListener {
            val callId = intent.getStringExtra("callId")
            callId?.let {
                declineCall(it)
                finish()
            }

        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(baseContext).registerReceiver(
            (mMessageReceiver),
            IntentFilter("CallReceiverData")
        )
    }

    override fun onStop() {
        super.onStop()
        ringtone.stop()
        LocalBroadcastManager.getInstance(baseContext).unregisterReceiver(mMessageReceiver)
    }

    private fun accept() {

    }

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.getStringExtra("action") == "ON_CALL_INTERRUPTED")
                finish()
        }
    }

    private fun declineCall(callId: String) {
        HomeClient().getHomeApiService(baseContext).declineCall(callId)
            .enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        println(response.body()?.string())
                    } else
                        println(response.errorBody()?.string())

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Constants.showError(t)
                }

            })
    }

}

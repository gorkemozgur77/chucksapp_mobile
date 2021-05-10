package com.example.chuckapp.modules.call

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.chuckapp.BaseActivity
import com.example.chuckapp.R
import com.example.chuckapp.model.friend.Friend
import com.example.chuckapp.modules.home.service.HomeClient
import com.example.chuckapp.modules.twilio.VideoActivity
import com.example.chuckapp.util.Constants
import kotlinx.android.synthetic.main.activity_call_reciever.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response


class CallReceiverActivity : BaseActivity() {

    private val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
    private lateinit var ringtone: Ringtone

    var friend = Friend("a", "a", "a")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_reciever)
        ringtone = RingtoneManager.getRingtone(applicationContext, notification)
        callerTextView.text = intent.getStringExtra("callerFullName")
        ringtone.play()
        val callId = intent.getStringExtra("callId")
        acceptCallFAB.setOnClickListener {
            if (callId != null) {
                acceptCall(callId)
            }
        }

        declineCallFAB.setOnClickListener {

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

    override fun getWebSocketData(jsonObject: JSONObject) {

    }


    val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.getStringExtra("action") == "ON_CALL_INTERRUPTED")
                finish()
        }
    }
// data.accessToken
    //data.roomName

    private fun acceptCall(callId: String) {
        HomeClient().getHomeApiService(baseContext).acceptCall(callId)
            .enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    println(response.errorBody()?.string())
                    if (response.isSuccessful){
                        val jsonObject = JSONObject(response.body()!!.string())
                        val accessToken = jsonObject.getJSONObject("data").getString("accessToken")
                        val roomName = jsonObject.getJSONObject("data").getString("roomName")
                        val videoIntent = Intent(baseContext, VideoActivity::class.java)
                        videoIntent.putExtra("room_name", roomName)
                        videoIntent.putExtra("access_token", accessToken)
                        startActivity(videoIntent)
                        finish()
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Constants.showError(t)
                    finish()
                }

            })
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

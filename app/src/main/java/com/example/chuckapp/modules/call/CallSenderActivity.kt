package com.example.chuckapp.modules.call

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.chuckapp.R
import com.example.chuckapp.model.friend.Friend
import com.example.chuckapp.modules.home.service.HomeClient
import com.example.chuckapp.util.Constants
import kotlinx.android.synthetic.main.activity_call_sender.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class CallSenderActivity : AppCompatActivity() {

    lateinit var callId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_sender)

        val friend = intent.getSerializableExtra("friend") as Friend
        intent.getStringExtra("call_id")?.let {
            callId = it
        }

        textView2.text = friend.fullName

        endCallFAB.setOnClickListener {
            interrupt()
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(baseContext).registerReceiver(
            (mMessageReceiver),
            IntentFilter("CallSenderData")
        )
        val timer = object : CountDownTimer(6000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                interrupt()
                finish()
            }

        }
        timer.start()


    }

    private fun interrupt() {
        HomeClient().getHomeApiService(baseContext).interrupt(callId)
            .enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {

                    println("-------- Interraptlandı ")
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Constants.showError(t)
                }

            })
    }

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            println("From broadcast    ------     " + intent.extras?.get("userId"))
            if (intent.getStringExtra("action") == "ON_CALL_DECLINED")
                finish()
        }
    }
}
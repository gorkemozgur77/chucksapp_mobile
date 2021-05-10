package com.example.chuckapp

import android.app.ActivityManager
import android.app.Dialog
import android.content.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.chuckapp.service.StatusService
import com.example.chuckapp.util.ProgressDialog
import org.json.JSONObject


abstract class BaseActivity : AppCompatActivity() {

    var progressBar: Dialog? = null
    private var isInBackground = false

    val homepageStatue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressBar = ProgressDialog(this)
    }

    fun showProgressBar() {
        progressBar?.show()
    }

    fun hideProgressBar() {
        progressBar?.hide()
    }

    override fun onStart() {
        if (isInBackground) {
            Intent(this, StatusService::class.java).also {
                startService(it)
            }
            LocalBroadcastManager.getInstance(baseContext).registerReceiver(
                (messageReceiver),
                IntentFilter("StatusChangeData")
            )
            isInBackground = false
        }
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        if (isApplicationBroughtToBackground()) {
            isInBackground = true
            LocalBroadcastManager.getInstance(baseContext).unregisterReceiver(messageReceiver)
            Intent(this, StatusService::class.java).also {
                stopService(it)
            }
        }
    }

    private fun isApplicationBroughtToBackground(): Boolean {
        val am =
            applicationContext.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val tasks = am.getRunningTasks(1)
        if (tasks.isNotEmpty()) {
            val topActivity: ComponentName? = tasks[0].topActivity
            if (topActivity != null) {
                if (topActivity.packageName != applicationContext.packageName) {
                    return true
                }
            }
        }
        return false
    }

    abstract fun getWebSocketData(jsonObject: JSONObject)

    val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val event = intent.getStringExtra("event")
            if (event != null) {
                getWebSocketData(JSONObject(event))
            }
        }
    }
}
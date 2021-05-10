package com.example.chuckapp.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.chuckapp.modules.home.view.HomePageActivity
import com.example.chuckapp.modules.home.view.appBarNavigation.AddFriendPage
import com.example.chuckapp.service.websocket.MainInteractor
import com.example.chuckapp.service.websocket.MainRepository
import com.example.chuckapp.service.websocket.WebServicesProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap

class StatusService : Service() {

    private var broadcaster: LocalBroadcastManager? = null
    private var interactor: MainInteractor? = null



    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    @ExperimentalCoroutinesApi
    override fun onCreate() {
        super.onCreate()


        interactor = MainInteractor(MainRepository(WebServicesProvider(applicationContext)))
        broadcaster = LocalBroadcastManager.getInstance(applicationContext)
        subscribeToSocketEvents()
    }



    @ExperimentalCoroutinesApi
    override fun onDestroy() {
        super.onDestroy()
        interactor?.stopSocket()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    @ExperimentalCoroutinesApi
    fun subscribeToSocketEvents() {
        val statusIntent = Intent(STATUS_CHANGE_DATA)
        GlobalScope.launch(Dispatchers.IO) {
            try {
                interactor?.startSocket()?.consumeEach {
                    if (it.exception == null) {
                        val decodedBytes = Base64.getDecoder().decode(it.text)
                        val messageJson = JSONObject(String(decodedBytes))
                        statusIntent.putExtra("event", messageJson.toString())
                        broadcaster?.sendBroadcast(statusIntent)

                    } else {
                        onSocketError(it.exception)
                    }
                }
            } catch (ex: java.lang.Exception) {
                onSocketError(ex)
            }
        }
    }

    private fun onSocketError(ex: Throwable) {
        println("Error occurred : ${ex.message}")
    }

    companion object {
        const val STATUS_CHANGE_DATA = "StatusChangeData"

        private var handlers = HashMap<String, HandlerMe>()
        private lateinit var currentHandler : HandlerMe

        fun setPage(page : String){
            handlers[page]?.let {
                currentHandler = it
            }
        }

        init {
            val activity = HomePageActivity()

//            handlers["page2"] = AddFriendPage()
        }
    }
}

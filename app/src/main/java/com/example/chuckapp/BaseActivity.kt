package com.example.chuckapp

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.chuckapp.model.requestModels.Home.ActiveInactiveResponse
import com.example.chuckapp.modules.home.service.HomeClient
import com.example.chuckapp.util.Constants
import com.example.chuckapp.util.ProgressDialog
import retrofit2.Call
import retrofit2.Response

open class BaseActivity : AppCompatActivity(), LifecycleObserver {

    var isUserOnline = false
    var progressBar: Dialog? = null

    var boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        progressBar = applicationContext?.let {
            ProgressDialog(it)
        }

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)


    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        println("offLINE")
        boolean = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        if (boolean) {
            boolean = false
            println("ONLINE")
        }


    }


    private fun setUserActive() {
        HomeClient().getHomeApiService(this).setOnline().enqueue(object :
            retrofit2.Callback<ActiveInactiveResponse> {
            override fun onResponse(
                call: Call<ActiveInactiveResponse>,
                response: Response<ActiveInactiveResponse>
            ) {
                if (response.isSuccessful)
                    println(response.body())
                else
                    println(response.errorBody()?.string())
            }

            override fun onFailure(call: Call<ActiveInactiveResponse>, t: Throwable) {
                Constants.showError(t)
            }
        })
    }

    private fun setUserInactive() {
        HomeClient().getHomeApiService(this).setOffline().enqueue(object :
            retrofit2.Callback<ActiveInactiveResponse> {
            override fun onResponse(
                call: Call<ActiveInactiveResponse>,
                response: Response<ActiveInactiveResponse>
            ) {
                if (response.isSuccessful)
                    println(response.body())
                else
                    println(response.errorBody()?.string())
            }

            override fun onFailure(call: Call<ActiveInactiveResponse>, t: Throwable) {
                Constants.showError(t)
            }
        })
    }
}
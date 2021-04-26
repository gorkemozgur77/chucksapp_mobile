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
import com.example.chuckapp.util.hideKeyboard
import retrofit2.Call
import retrofit2.Response

open class BaseActivity : AppCompatActivity(), LifecycleObserver {

    var isUserOnline = false
    var progressBar: Dialog? = null

    var wasInBackground = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        progressBar = applicationContext?.let {
            ProgressDialog(it)
        }
        ProcessLifecycleOwner.get().lifecycle.addObserver(this);


    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        // app moved to foreground
        wasInBackground = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onMoveToBackground() {
        // app moved to background
        wasInBackground = false
    }

    override fun onStop() {
        super.onStop()

        if (!wasInBackground)
            setUserInactive()
    }

    override fun onStart() {
        super.onStart()
        if (!wasInBackground)
            setUserActive()
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
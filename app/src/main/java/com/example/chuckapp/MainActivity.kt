package com.example.chuckapp

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chuckapp.model.requestModels.auth.LoginRequestViaToken
import com.example.chuckapp.model.requestModels.auth.LoginResponseViaToken
import com.example.chuckapp.modules.auth.service.AuthClient
import com.example.chuckapp.service.SessionManager
import com.example.chuckapp.util.Constants
import com.example.chuckapp.modules.auth.view.AuthActivity
import com.example.chuckapp.modules.home.HomePageActivity
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var authClient : AuthClient
    private lateinit var sessionmanager : SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val background = object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(3000)
                    observeLogin(applicationContext)
                    println("yapildi")
                }catch (e : Exception){
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }

    fun observeLogin(context : Context){
        authClient = AuthClient()
        sessionmanager = SessionManager(context)
        if (sessionmanager.fetchPermToken() != null){
            authClient.getAuthApiService().signInViaToken(
                    LoginRequestViaToken(sessionmanager.fetchPermToken()!!, Constants.PLATFORM_NAME))
                    .enqueue(object : retrofit2.Callback<LoginResponseViaToken>{
                        override fun onResponse(call: Call<LoginResponseViaToken>, response: Response<LoginResponseViaToken>) {
                            if (response.isSuccessful){
                                println(response.body().toString())
                                response.body()!!.token.Token?.let { sessionmanager.saveAuthToken(it) }
                                val intent = Intent(baseContext, HomePageActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else {
                                println(response.errorBody()!!.string())
                                val intent = Intent(baseContext, AuthActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                        override fun onFailure(call: Call<LoginResponseViaToken>, t: Throwable) {
                            println("onFailure a dustu")
                        }
                    })
        }
        else {
            val intent = Intent(baseContext, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }

    }


}
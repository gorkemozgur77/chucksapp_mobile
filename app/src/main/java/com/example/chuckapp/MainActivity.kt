package com.example.chuckapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chuckapp.model.requestModels.auth.LoginRequestViaToken
import com.example.chuckapp.model.requestModels.auth.LoginResponseViaToken
import com.example.chuckapp.modules.auth.service.AuthClient
import com.example.chuckapp.modules.auth.view.AuthActivity
import com.example.chuckapp.modules.home.view.HomePageActivity
import com.example.chuckapp.service.SessionManager
import com.example.chuckapp.util.Constants
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var sessionmanager : SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(3000)
                    observeLogin(applicationContext)
                    println("SplashScreen gecti.")
                }catch (e : Exception){
                    e.printStackTrace()
                }
            }
        }.start()

    }

    private fun observeLogin(context : Context){
        sessionmanager = SessionManager(context)
        if (sessionmanager.fetchPermToken() != null){
            AuthClient().getAuthApiService().signInViaToken(LoginRequestViaToken(sessionmanager.fetchPermToken()!!, Constants.PLATFORM_NAME)).enqueue(signInViaTokenHandler)
        }
        else {
            startActivity(Intent(baseContext, AuthActivity::class.java))
            finish()
        }

    }

    private val signInViaTokenHandler = object : retrofit2.Callback<LoginResponseViaToken>{
        override fun onResponse(call: Call<LoginResponseViaToken>, response: Response<LoginResponseViaToken>) {
            if (response.isSuccessful){
                println(response.body().toString())
                response.body()!!.token.Token?.let { sessionmanager.saveAuthToken(it) }
                startActivity(Intent(baseContext, HomePageActivity::class.java))
                finish()
            }
            else {
                println(response.errorBody()!!.string())
                startActivity(Intent(baseContext, AuthActivity::class.java))
                finish()
            }
        }
        override fun onFailure(call: Call<LoginResponseViaToken>, t: Throwable) {
            println("onFailure a dustu")
        }
    }


}
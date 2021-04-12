package com.example.chuckapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chuckapp.model.FirebaseToken
import com.example.chuckapp.model.requestModels.auth.LoginRequestViaToken
import com.example.chuckapp.model.requestModels.auth.LoginResponseViaToken
import com.example.chuckapp.modules.auth.service.AuthClient
import com.example.chuckapp.modules.auth.view.AuthActivity
import com.example.chuckapp.modules.home.service.HomeClient
import com.example.chuckapp.modules.home.view.HomePageActivity
import com.example.chuckapp.service.SessionManager
import com.example.chuckapp.util.Constants
import com.google.firebase.messaging.FirebaseMessaging
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var sessionmanager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sessionmanager = SessionManager(this)
        object : Thread() {
            override fun run() {
                try {
                    sleep(3000)
                    observeLogin()
                    println("SplashScreen gecti.")

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }.start()

    }

    private fun observeLogin() {

        if (sessionmanager.fetchPermToken() != null) {
            AuthClient().getAuthApiService().signInViaToken(
                LoginRequestViaToken(
                    sessionmanager.fetchPermToken()!!,
                    Constants.PLATFORM_NAME
                )
            ).enqueue(signInViaTokenHandler)
        } else {
            startActivity(Intent(baseContext, AuthActivity::class.java))
            finish()
        }

    }

    fun sendFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful && it.result != null)
                HomeClient().getHomeApiService(this)
                    .sendFirebaseToken(FirebaseToken(it.result!!))
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

    private val signInViaTokenHandler = object : retrofit2.Callback<LoginResponseViaToken> {
        override fun onResponse(
            call: Call<LoginResponseViaToken>,
            response: Response<LoginResponseViaToken>
        ) {
            if (response.isSuccessful) {
                println(response.body().toString())
                response.body()!!.token.Token?.let { sessionmanager.saveAuthToken(it) }
                sendFirebaseToken()
                startActivity(Intent(baseContext, HomePageActivity::class.java))
                finish()
            } else {
                println(response.errorBody()!!.string())
                startActivity(Intent(baseContext, AuthActivity::class.java))
                finish()
            }
        }

        override fun onFailure(call: Call<LoginResponseViaToken>, t: Throwable) {
            Constants.showError(t)
        }
    }


}
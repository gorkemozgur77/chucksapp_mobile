package com.example.chuckapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chuckapp.model.requestModels.auth.LoginRequestViaToken
import com.example.chuckapp.model.requestModels.auth.LoginResponseViaToken
import com.example.chuckapp.service.ApiClient
import com.example.chuckapp.service.SessionManager
import com.example.chuckapp.util.Constants
import com.example.chuckapp.modules.auth.view.AuthActivity
import com.example.chuckapp.modules.home.HomePageActivity
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var apiclient : ApiClient
    private lateinit var sessionmanager : SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val background = object : Thread() {
            override fun run() {
                try {
                    //threaad 5 sn yani 5000 ms uyusun
                    Thread.sleep(3000)
                    //intent ile splash ekranından sonra MainActivity ekranı açılsın diyoruz
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

        apiclient = ApiClient()
        sessionmanager = SessionManager(context)

        if (sessionmanager.fetchPermToken() != null){
            apiclient.getAuthApiService().signInViaToken(
                    LoginRequestViaToken(sessionmanager.fetchPermToken()!!, Constants.PLATFORM_NAME))
                    .enqueue(object : retrofit2.Callback<LoginResponseViaToken>{
                        override fun onResponse(call: Call<LoginResponseViaToken>, response: Response<LoginResponseViaToken>) {
                            if (response.body()!=null){
                                println(response.body().toString())
                                response.body()!!.token.Token?.let { sessionmanager.saveAuthToken(it) }
                                val intent = Intent(baseContext, HomePageActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else if (response.errorBody() != null){
                                println(response.errorBody()!!.string())
                                val intent = Intent(baseContext, AuthActivity::class.java)
                                startActivity(intent)
                                finish()
                            }


                        }

                        override fun onFailure(call: Call<LoginResponseViaToken>, t: Throwable) {


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
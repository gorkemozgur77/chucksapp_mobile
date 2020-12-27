package com.example.chuckapp.modules.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.chuckapp.R
import com.example.chuckapp.model.requestModels.auth.LoginResponse
import com.example.chuckapp.model.requestModels.signUp.MeResponse
import com.example.chuckapp.model.requestModels.signUp.SignOutRequest
import com.example.chuckapp.modules.auth.validator.SignInValidator
import com.example.chuckapp.modules.home.service.HomeClient
import com.example.chuckapp.service.SessionManager
import com.example.chuckapp.util.Constants
import com.example.chuckapp.modules.auth.view.AuthActivity
import kotlinx.android.synthetic.main.activity_home_page.*
import retrofit2.Call
import retrofit2.Response

class HomePageActivity : AppCompatActivity() {

    private lateinit var apiclient : HomeClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        getInfo(applicationContext)
        button.setOnClickListener {
            signOut(applicationContext,window.decorView.rootView)
        }
    }
    fun signOut(context: Context,view : View){
        apiclient = HomeClient()
        val sessionManager = SessionManager(context)
        apiclient.getHomeApiService(context).signOut(SignOutRequest(Constants.PLATFORM_NAME)).enqueue(object : retrofit2.Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                println(response.body())
                println(response.errorBody()?.string())
                if (response.isSuccessful){
                    println("cikis yapildi")
                    sessionManager.deleteTokens()
                    val intent = Intent(baseContext, AuthActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                println(t.cause)
                println(t.message)
                print(t.stackTrace)
            }
        })
    }


    fun getInfo(context: Context){

        apiclient = HomeClient()

        apiclient.getHomeApiService(context).getProfile().enqueue(object :retrofit2.Callback<MeResponse>{
            override fun onResponse(call: Call<MeResponse>, response: Response<MeResponse>) {
                if (response.isSuccessful){
                    textView3.text = response.body()?.user?.isim.toString() + " " + response.body()?.user?.soyisim.toString()
                    textView2.text = response.body()?.user?.eposta.toString()
                }
            }

            override fun onFailure(call: Call<MeResponse>, t: Throwable) {

            }

        })
    }

}



package com.example.chuckapp.modules.auth.service

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ProgressBar
import com.example.chuckapp.model.requestModels.auth.LoginResponse
import com.example.chuckapp.modules.auth.validator.SignInValidator
import com.example.chuckapp.modules.home.view.HomePageActivity
import com.example.chuckapp.service.SessionManager
import retrofit2.Call
import retrofit2.Response

class GorkemRespHandler(
        val view: View,
        val context: Context,
        private val loginProgressbar: ProgressBar,
        private val activity: Activity
) : retrofit2.Callback<LoginResponse> {

    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
        val sessionmanager = SessionManager(context)
        val errorValidator = SignInValidator()
        loginProgressbar.visibility = View.GONE
        if(response.isSuccessful){
            println(response.body())
            response.body()!!.token?.PermToken?.let { sessionmanager.savePermToken(it) }
            response.body()!!.token?.Token?.let { sessionmanager.saveAuthToken(it) }
            val intent = Intent(context, HomePageActivity::class.java)
            context.startActivity(intent)
            activity.finish()
        }
        else {
            errorValidator.wrongInfoAlertDialog(view)
            val string = response.errorBody()?.string()
            println(string)
        }
    }

    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
        println("Servera baglanmadik")
    }


}
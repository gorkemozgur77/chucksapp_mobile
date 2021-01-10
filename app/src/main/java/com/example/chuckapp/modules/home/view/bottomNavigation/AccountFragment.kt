package com.example.chuckapp.modules.home.view.bottomNavigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chuckapp.R
import com.example.chuckapp.model.requestModels.auth.LoginResponse
import com.example.chuckapp.model.requestModels.signUp.MeResponse
import com.example.chuckapp.model.requestModels.signUp.SignOutRequest
import com.example.chuckapp.modules.auth.view.AuthActivity
import com.example.chuckapp.modules.home.service.HomeClient
import com.example.chuckapp.service.SessionManager
import com.example.chuckapp.util.Constants
import kotlinx.android.synthetic.main.fragment_account.*
import retrofit2.Call
import retrofit2.Response


class AccountFragment : Fragment() {

    private lateinit var apiclient : HomeClient
    private lateinit var name : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let {
            getInfo(it)
        }
        accountLogOutbuttonId.setOnClickListener {
            context?.let { it1 -> signOut(it1) }
        }
    }

    fun getInfo(context: Context){
        accountProgressBar.visibility = View.VISIBLE
        apiclient = HomeClient()
        apiclient.getHomeApiService(context).getProfile().enqueue(object :retrofit2.Callback<MeResponse>{
            override fun onResponse(call: Call<MeResponse>, response: Response<MeResponse>) {
                accountProgressBar.visibility = View.GONE
                if (response.isSuccessful){
                    accountNameAndSurnameId.text = response.body()?.user?.isim.toString() + " " + response.body()?.user?.soyisim.toString()
                    accountEmailId.text = response.body()?.user?.eposta.toString()
                }
            }
            override fun onFailure(call: Call<MeResponse>, t: Throwable) {
                Constants.showError(t)
            }


        })


    }
    fun signOut(context: Context){
        apiclient = HomeClient()
        val sessionManager = SessionManager(context)
        apiclient.getHomeApiService(context).signOut(SignOutRequest(Constants.PLATFORM_NAME)).enqueue(object : retrofit2.Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                println(response.body())
                println(response.errorBody()?.string())

                if (response.isSuccessful){
                    println("cikis yapildi")
                    sessionManager.deleteTokens()
                    startActivity(Intent(context, AuthActivity::class.java))
                    activity?.finish()
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Constants.showError(t)
            }
        })
    }

}
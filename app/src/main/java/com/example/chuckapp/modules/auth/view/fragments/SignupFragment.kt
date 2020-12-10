package com.example.chuckapp.modules.auth.view.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.chuckapp.R
import com.example.chuckapp.model.User
import com.example.chuckapp.model.requestModels.signUp.SignResponse
import com.example.chuckapp.service.ApiClient
import com.example.chuckapp.service.SessionManager
import com.example.chuckapp.modules.auth.util.AuthUtil
import com.example.chuckapp.util.Constants
import com.example.chuckapp.modules.auth.validator.RegisterAuthValidator
import com.example.chuckapp.modules.home.HomePageActivity
import kotlinx.android.synthetic.main.fragment_signup.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response


class SignupFragment : Fragment() {
    private lateinit var apiClient: ApiClient
    private val authUtil = AuthUtil()
    private val layoutErrorValidator = RegisterAuthValidator()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar.visibility = View.GONE

        signButton.setOnClickListener {

             signUp(it.context)
        }
        nameId.setOnFocusChangeListener { v, hasFocus ->
            if (v.isFocused){
                nameLayId.error = null
            }
        }
        surnameId.setOnFocusChangeListener { v, hasFocus ->
            if (v.isFocused){
                surnameLayId.error = null
            }
        }
        emailId.setOnFocusChangeListener { v, hasFocus ->
            if (v.isFocused){
                emailLayId.error = null
            }
        }

        passWordId.setOnFocusChangeListener { v, hasFocus ->
            if (v.isFocused){
                passwordLayId.error = null
            }
        }
    }



    fun signUp(context: Context){

        layoutErrorValidator.layoutEmptyErrorValidator(nameLayId, nameId)
        layoutErrorValidator.layoutEmptyErrorValidator(surnameLayId, surnameId)
        layoutErrorValidator.layoutEmailRegexValidator(emailLayId, emailId)
        layoutErrorValidator.layoutEmptyErrorValidator(passwordLayId, passWordId)


        progressBar.visibility = View.VISIBLE
        var sessionmanager = SessionManager(context)
        apiClient = ApiClient()

        apiClient.getAuthApiService().signUp(getUserInfo(context)).enqueue(object : retrofit2.Callback<SignResponse>{
            override fun onResponse(call: Call<SignResponse>, response: Response<SignResponse>) {
                progressBar.visibility = View.GONE
                if(response.isSuccessful){
                    response.body()?.tokenlar?.Token?.let { sessionmanager.saveAuthToken(it) }
                    response.body()?.tokenlar?.PermToken?.let { sessionmanager.savePermToken(it) }
                    println(response.body())

                    val intent = Intent(context, HomePageActivity::class.java)
                    startActivity(intent)
                    activity?.finish()


                } else {
                    try {
                        println(response.errorBody()?.string())
                        var hata = JSONObject(response.errorBody()!!.string())
                        if(hata.getString("message") == "Email already exists"){
                            emailLayId.isErrorEnabled = true
                            emailLayId.error = ""
                        }
                    } catch (e : Exception){
                        println(e.message)
                    }

                }
            }
            override fun onFailure(call: Call<SignResponse>, t: Throwable) {
                 signButton.text= "faile dustu"
                 progressBar.visibility = View.GONE

            }
        })
    }


    fun getUserInfo(context: Context) : User{
        val user = User(
                nameId.text.toString(),
                surnameId.text.toString(),
                emailId.text.toString(),
                passWordId.text.toString(),
                Constants.PLATFORM_NAME,
                authUtil.getIpAdress(context),
                authUtil.getMacAdress(context),
                authUtil.getDeviceName())
        return user
    }


}
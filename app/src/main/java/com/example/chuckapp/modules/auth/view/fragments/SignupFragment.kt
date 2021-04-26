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
import com.example.chuckapp.model.requestModels.signUp.EmailExitanceResponce
import com.example.chuckapp.model.requestModels.signUp.SignResponse
import com.example.chuckapp.modules.auth.service.AuthClient
import com.example.chuckapp.modules.auth.util.AuthUtil
import com.example.chuckapp.modules.auth.validator.RegisterAuthValidator
import com.example.chuckapp.modules.home.view.HomePageActivity
import com.example.chuckapp.service.SessionManager
import com.example.chuckapp.util.Constants
import kotlinx.android.synthetic.main.fragment_signup.*
import retrofit2.Call
import retrofit2.Response


class SignupFragment : Fragment() {
    private lateinit var authClient: AuthClient
    private val authUtil = AuthUtil()
    private val layoutErrorValidator = RegisterAuthValidator()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar.visibility = View.GONE
        emailLayId.isEndIconVisible = false

        signButton.setOnClickListener { signUp(it.context) }

        nameId.setOnFocusChangeListener { v, hasFocus ->
            if (v.isFocused) {
                nameLayId.isErrorEnabled = false
                nameLayId.error = null
            }
        }

        surnameId.setOnFocusChangeListener { v, hasFocus ->
            if (v.isFocused) {
                surnameLayId.isErrorEnabled = false
                surnameLayId.error = null
            }
        }

        emailId.setOnFocusChangeListener { v, hasFocus ->
            if (v.isFocused) {
                emailLayId.isErrorEnabled = false
                emailLayId.error = null
                emailLayId.helperText = null
                emailLayId.isEndIconVisible = false
            } else {
                layoutErrorValidator.layoutEmailRegexValidator(emailLayId, emailId)
                if (emailId.text?.length!! > 0 && !emailLayId.isErrorEnabled)
                    checkExistance(emailId.text.toString())
            }
        }

        passWordId.setOnFocusChangeListener { v, hasFocus ->
            if (v.isFocused) {
                passwordLayId.isErrorEnabled = false
                passwordLayId.error = null
            }
        }
    }

    private fun checkExistance(email: String) {
        emailProgressbar.visibility = View.VISIBLE
        authClient = AuthClient()
        authClient.getAuthApiService().checkEmailExistance(email)
            .enqueue(object : retrofit2.Callback<EmailExitanceResponce> {
                override fun onResponse(
                    call: Call<EmailExitanceResponce>,
                    response: Response<EmailExitanceResponce>
                ) {
                    emailProgressbar.visibility = View.GONE
                    if (response.isSuccessful) {
                        println(response.body())
                        if (response.body()?.existance == "false") {
                            emailLayId.endIconDrawable =
                                resources.getDrawable(R.drawable.ic_baseline_check_circle_24)
                            emailLayId.helperText = "Valid Email"
                            emailLayId.isEndIconVisible = true
                        } else layoutErrorValidator.errorAction(emailLayId, "Email already exists")
                    }
                }

                override fun onFailure(call: Call<EmailExitanceResponce>, t: Throwable) {
                    println("Email existance request failledi")
                }
            })
    }

    private fun validateLayouts() {
        layoutErrorValidator.layoutEmptyErrorValidator(nameLayId, nameId)
        layoutErrorValidator.layoutEmptyErrorValidator(surnameLayId, surnameId)
        layoutErrorValidator.layoutEmailRegexValidator(emailLayId, emailId)
        layoutErrorValidator.layoutPasswordValidator(passwordLayId, passWordId)
    }

    private fun getUserInfo(context: Context): User {
        return User(
            nameId.text.toString(),
            surnameId.text.toString(),
            emailId.text.toString(),
            passWordId.text.toString(),
            Constants.PLATFORM_NAME,
            authUtil.getIpAdress(context),
            authUtil.getMacAdress(context),
            authUtil.getDeviceName(),
            friends = listOf(),
            friendRequestInbox = null
        )
    }

    private fun sendSignUpRequest(context: Context) {
        progressBar.visibility = View.VISIBLE
        var sessionmanager = SessionManager(context)
        authClient = AuthClient()
        authClient.getAuthApiService().signUp(getUserInfo(context))
            .enqueue(object : retrofit2.Callback<SignResponse> {
                override fun onResponse(
                    call: Call<SignResponse>,
                    response: Response<SignResponse>
                ) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {

                        response.body()?.tokenlar?.Token?.let { sessionmanager.saveAuthToken(it) }
                        response.body()?.tokenlar?.PermToken?.let { sessionmanager.savePermToken(it) }
                        println(response.body())

                        val intent = Intent(context, HomePageActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                }

                override fun onFailure(call: Call<SignResponse>, t: Throwable) {
                    signButton.text = "faile dustu"
                    progressBar.visibility = View.GONE
                }
            })
    }

    private fun signUp(context: Context) {
        println("s")
        validateLayouts()
        if (nameLayId.isErrorEnabled || surnameLayId.isErrorEnabled || emailLayId.isErrorEnabled || passwordLayId.isErrorEnabled)
            return
        sendSignUpRequest(context)
    }


}
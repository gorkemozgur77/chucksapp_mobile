package com.example.chuckapp.modules.auth.view.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.chuckapp.R
import com.example.chuckapp.model.FirebaseToken
import com.example.chuckapp.model.requestModels.auth.LoginRequest
import com.example.chuckapp.model.requestModels.auth.LoginResponse
import com.example.chuckapp.modules.auth.service.AuthClient
import com.example.chuckapp.modules.auth.util.AuthUtil
import com.example.chuckapp.modules.auth.validator.SignInValidator
import com.example.chuckapp.modules.home.service.HomeClient
import com.example.chuckapp.modules.home.view.HomePageActivity
import com.example.chuckapp.service.SessionManager
import com.example.chuckapp.util.Constants
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.fragment_login.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response


class LoginFragment : Fragment() {

    private lateinit var authClient: AuthClient
    private val authUtil = AuthUtil()
    private val errorValidator = SignInValidator()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let { SessionManager(it) }
        textButton.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
            Navigation.findNavController(it).navigate(action)
        }
        loginButton.setOnClickListener {
            signIn(it.context)
        }
        loginEmailId.setOnFocusChangeListener { v, hasFocus ->
            if (v.isFocused) {
                loginEmailLayId.isErrorEnabled = false
                loginEmailLayId.error = null
            }
        }

        loginPasswordId.setOnFocusChangeListener { v, hasFocus ->
            if (v.isFocused) {
                loginPasswordLayId.isErrorEnabled = false
                loginPasswordLayId.error = null
            }
        }
    }

    private fun signIn(context: Context) {
        validateLayouts()
        if (loginEmailLayId.isErrorEnabled || loginPasswordLayId.isErrorEnabled)
            return
        signInViaMail(context)
    }

    private fun validateLayouts() {
        errorValidator.layoutEmailRegexValidator(loginEmailLayId, loginEmailId)
        errorValidator.layoutEmptyErrorValidator(loginPasswordLayId, loginPasswordId)
    }

    private fun signInViaMail(context: Context) {
        loginProgressbar.visibility = View.VISIBLE
        authClient = AuthClient()
        authClient.getAuthApiService().signInViaEmail(getInfo(context))
            .enqueue(signInViaMailHandler)

    }
    fun sendFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful && it.result != null)
                context?.let { it1 ->
                    HomeClient().getHomeApiService(it1)
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


    }
    private val signInViaMailHandler = object : retrofit2.Callback<LoginResponse> {

        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
            loginProgressbar.visibility = View.GONE
            if (response.isSuccessful) {
                println(response.body())
                sendFirebaseToken()
                response.body()!!.token?.PermToken?.let {
                    context?.let { it1 ->
                        SessionManager(it1).savePermToken(
                            it
                        )
                    }
                }
                response.body()!!.token?.Token?.let {
                    context?.let { it1 ->
                        SessionManager(it1).saveAuthToken(
                            it
                        )
                    }
                }
                startActivity(Intent(context, HomePageActivity::class.java))
                activity?.finish()
            } else {
                view?.let { errorValidator.wrongInfoAlertDialog(it) }
                println(response.errorBody()?.string())
            }
        }

        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            println(t.message + t.stackTrace + t.cause + t.localizedMessage)
        }
    }

    private fun getInfo(context: Context): LoginRequest {

        return LoginRequest(
            loginEmailId.text.toString(),
            loginPasswordId.text.toString(),
            Constants.PLATFORM_NAME,
            authUtil.getIpAddress(context),
            authUtil.getMacAddress(context),
            authUtil.getDeviceName()
        )
    }
}